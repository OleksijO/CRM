package com.becomejavasenior.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.becomejavasenior.service.SpringServiceTests.APPLICATION_CONTEXT_FILE;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(APPLICATION_CONTEXT_FILE)

abstract public class SpringServiceTests {
    public static final String APPLICATION_CONTEXT_FILE = "classpath:test-services.xml";

}
