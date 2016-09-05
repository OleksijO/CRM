import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Deal;
import com.becomejavasenior.entity.Report;
import com.becomejavasenior.entity.User;
import com.becomejavasenior.jdbc.entity.CompanyDAO;
import com.becomejavasenior.jdbc.entity.DealDAO;
import com.becomejavasenior.jdbc.entity.ReportDAO;
import com.becomejavasenior.jdbc.entity.UserDAO;
import com.becomejavasenior.service.ReportService;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-services.xml")
public class ReportServiceTest {
    @Autowired
    private CompanyDAO companyDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private DealDAO dealDAO;
    @Autowired
    private ReportDAO reportDAO;
    @Autowired
    private ReportService reportService;
    private BigDecimal amount[] = new BigDecimal[3];


    private User userForDealTest;
    private Company companyForTest;
    private int dealTestId;
    private List<Deal> dealList = new ArrayList<>();
    private List<Company> companyList = new ArrayList<>();
    private int maxDeals = 9;
    private int maxCompanies = 3;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");

    @PostConstruct
    public void init() {
        companyForTest = companyDAO.getById(1);
        userForDealTest = userDAO.getById(1);
        companyForTest.setResponsibleUser(userForDealTest);
    }

    @Test
    public void testGetDealsAmountForPreviousHour() {
        //prepare DB
        System.out.println(">>>>>>>>>>>>>>>  SET UP   >>>>>>>>>>>>>>>>>");

        amount[0] = new BigDecimal(0);
        amount[1] = new BigDecimal(0);
        amount[2] = new BigDecimal(0);
        for (int i = 0; i < maxCompanies; i++) {
            Company company = companyDAO.getById(1);
            company.setId(0);
            company.setResponsibleUser(userForDealTest);
            company.setName("Company " + i);
            company.setId(companyDAO.insert(company));
            System.out.println("Added: " + company.toString());
            companyList.add(company);
        }
        for (int i = 0; i < maxDeals; i++) {
            Deal deal = dealDAO.getById(1);
            deal.setId(0);
            Date date = DateUtils.truncate(new Date(System.currentTimeMillis()), Calendar.HOUR);
            deal.setDateCreate(new Date(date.getTime() - 1000 * 60 * (i + 1)));
            deal.setAmount(new BigDecimal((Math.pow(10, i % 3) + i) * i));
            deal.setCompany(companyList.get(i % 3));
            deal= dealDAO.getById(dealDAO.insert(deal));
            deal.setCompany(companyList.get(i % 3));
            System.out.println("Added: " + deal.toString() + " | " + deal.getDate() + " | " + deal.getCompany().toString());
            amount[i % 3].add(deal.getAmount());
            dealList.add(deal);
        }
        System.out.println(">>>>>>>>>>  SET UP  >>>>> DONE !  ");
        //get test data
        int totalNumberOfReports=reportDAO.getAll().size();
        List<Report> listReport=reportDAO.makeReportsWithDealsAmountForPreviousHour();
        int numberOfTestReports=listReport.size();
        reportService.makeHourDealAmountReports();
        int newTotalNumberOfTestReports=listReport.size();
        //restore database
        for(Report report:listReport){
            reportDAO.delete(report.getId());
        }
        System.out.println(">>>>>>>>>>>>>>>  TEAR DOWN >>>>>>>>>>>>>>>>>>");
        List<Deal> dealsTODEleteList = new ArrayList<>();
        for (int i = 0; i < maxDeals; i++) {
            Deal deal = dealList.get(i);
            dealDAO.delete(deal.getId());
            if (dealDAO.getById(deal.getId()) == null) {
                System.out.println("Deleted: " + deal.toString());
                dealsTODEleteList.add(deal);

            }
        }
        List<Company> companiesTODEleteList = new ArrayList<>();
        for (int i = 0; i < maxCompanies; i++) {
            Company company = companyList.get(i);
            companyDAO.delete(company.getId());
            if (companyDAO.getById(company.getId()) == null) {
                System.out.println("Deleted: " + company.toString());
                companiesTODEleteList.add(company);
            }
        }
        dealList.removeAll(dealsTODEleteList);
        companyList.removeAll(companiesTODEleteList);
        System.out.println("-------------------------------------------------------------");
        if (dealList.size() + companyList.size() == 0) {
            System.out.println("------ Everything is cleared.");
        } else {
            System.out.println("------ Something left in database ---------- !!!!!!!!!!!!!!");
        }
        System.out.println("-------------------------------------------------------------");
        companyList.clear();
        dealList.clear();
        System.out.println(">>>>>>>>>>  TEAR DOWN  >>>>> DONE !  ");
        // perform checks
        Assert.assertEquals("Quantitity of added reports: ",
                numberOfTestReports,
                newTotalNumberOfTestReports-totalNumberOfReports);

    }


}
