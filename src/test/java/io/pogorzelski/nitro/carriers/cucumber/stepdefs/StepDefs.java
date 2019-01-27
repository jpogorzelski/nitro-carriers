package io.pogorzelski.nitro.carriers.cucumber.stepdefs;

import io.pogorzelski.nitro.carriers.NitroCarriersApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = NitroCarriersApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
