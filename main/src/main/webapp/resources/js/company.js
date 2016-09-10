$(document).ready(function () {
    $('#add_contact').click(function () {
        if ($(".contact").is(":hidden")) {
            $(".contact").show();
        } else {
            $(".contact").hide();
        }
    });
});

$(document).ready(function () {
    $('#add_deal').click(function () {
        if ($(".quick_deal").is(":hidden")) {
            $(".quick_deal").show();
        } else {
            $(".quick_deal").hide();
        }
    });
});

$(document).ready(function () {
    $('#dateTask').datetimepicker({
        pickTime: false,
        inline: true
    });
});

$(document).ready(function () {
    $('#companyForm').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            companyName: {
                validators: {
                    notEmpty: {
                        message: 'Имя компании не может быть пустым'
                    },
                    stringLength: {
                        max: 200,
                        message: 'Имя компании не может быть больше 200 символов'
                    }
                }
            },
            companyTag: {
                validators: {
                    notEmpty: {
                        message: 'Тэги не могут быть пустыми'
                    },
                    stringLength: {
                        max: 1000,
                        message: 'Тэги не могут быть больше 1000 символов'
                    }
                }
            },
            companyPhone: {
                validators: {
                    notEmpty: {
                        message: 'Телефон не может быть пустым'
                    },
                    stringLength: {
                        max: 45,
                        message: 'Телефон не может быть больше 45 символов'
                    }
                }
            },
            companyEmail: {
                validators: {
                    notEmpty: {
                        message: 'Email не может быть пустым'
                    },
                    stringLength: {
                        max: 320,
                        message: 'Email не может быть больше 320 символов'
                    }
                }
            },
            companyWeb: {
                validators: {
                    notEmpty: {
                        message: 'Web-сайт не может быть пустым'
                    },
                    stringLength: {
                        max: 255,
                        message: 'Web-сайт не может быть больше 255 символов'
                    }
                }
            },
            companyAddress: {
                validators: {
                    notEmpty: {
                        message: 'Адрес не может быть пустым'
                    },
                    stringLength: {
                        max: 200,
                        message: 'Адрес не может быть больше 200 символов'
                    }
                }
            },
            companyNote: {
                validators: {
                    stringLength: {
                        max: 500,
                        message: 'Примечание не может быть больше 500 символов'
                    }
                }
            },
            contactName: {
                validators: {
                    notEmpty: {
                        message: 'Имя и Фамилия не могут быть пустыми'
                    },
                    stringLength: {
                        max: 300,
                        message: 'Имя и Фамилия не могут быть больше 300 символов'
                    }
                }
            },
            contactPosition: {
                validators: {
                    stringLength: {
                        max: 100,
                        message: 'Должность не может быть больше 100 символов'
                    }
                }
            },
            contactPhone: {
                validators: {
                    stringLength: {
                        max: 45,
                        message: 'Телефон не может быть больше 45 символов'
                    }
                }
            },
            contactEmail: {
                validators: {
                    stringLength: {
                        max: 320,
                        message: 'Email не может быть больше 320 символов'
                    }
                }
            },
            contactSkype: {
                validators: {
                    stringLength: {
                        max: 32,
                        message: 'Skype не может быть больше 32 символов'
                    }
                }
            },
            dealName: {
                validators: {
                    notEmpty: {
                        message: 'Название сделки не может быть пустым'
                    },
                    stringLength: {
                        max: 200,
                        message: 'Название сделки не может быть больше 200 символов'
                    }
                }
            },
        }
    });
});