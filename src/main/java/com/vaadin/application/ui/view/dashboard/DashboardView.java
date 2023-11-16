package com.vaadin.application.ui.view.dashboard;
import com.vaadin.application.ui.view.list.ListView;

import com.vaadin.application.backend.service.CompanyService;
import com.vaadin.application.backend.service.ContactService;
import com.vaadin.application.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Vaadin CRM")
public class DashboardView extends VerticalLayout {
    private ContactService contactService;
    private CompanyService companyService;
    public DashboardView(ContactService contactService, CompanyService
            companyService) {
        this.contactService = contactService;
        this.companyService = companyService;
        add(getContactStats(), getCompaniesChart());

        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    //① contactService.count() gives us the number of contacts in the database. It returns
    //a Span with the count and a text explanation
    private Component getContactStats() {
        Span stats = new Span(contactService.count() + " contacts");
        stats.addClassName("contact-stats");
        return stats;
    }


    private Chart getCompaniesChart() {
        Chart chart = new Chart(ChartType.PIE);
        DataSeries dataSeries = new DataSeries();
        Map<String, Integer> companies = companyService.getStats();
        companies.forEach((company, employees) ->
                dataSeries.add(new DataSeriesItem(company, employees)));
        chart.getConfiguration().setSeries(dataSeries);
        return chart;
    }



}
