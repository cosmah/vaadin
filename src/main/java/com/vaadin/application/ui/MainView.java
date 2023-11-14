package com.vaadin.application.ui;

import com.vaadin.application.backend.entity.Company;
import com.vaadin.application.backend.entity.Contact;
import com.vaadin.application.backend.service.ContactService;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;


@Route("")
@CssImport("./themes/vaadin/styles.css")
public class MainView extends VerticalLayout {
    private ContactService contactService;
    private Grid<Contact> grid = new Grid<>(Contact.class);
    private TextField filterText = new TextField();

    //import contact form
    private ContactForm form;
    public MainView(ContactService contactService) {
        this.contactService = contactService;
        addClassName("list-view");
        setSizeFull();
        configureFilter();
        configureGrid();

        //call form constructor
        form = new ContactForm();

        //Creates a Div that wraps the grid and the form, gives it a CSS class name, and makes
        //it full size
        //Div content = new Div(grid, form);

        // Create a HorizontalLayout to hold the form and grid
        HorizontalLayout content = new HorizontalLayout(grid,form );
        content.setSizeFull();
        content.expand(grid);

        // Set width of the form
        form.setWidth("300px"); // Adjust width as needed



        // Adds the content layout to the main layout
        add(filterText, content);
        updateList();
    }

    //text field method
    private void configureFilter() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
    }
    private void updateList() {
        grid.setItems(contactService.findAll(filterText.getValue()));
    }
    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.removeColumnByKey("company");
        grid.setColumns("firstName", "lastName", "email", "status");
        grid.addColumn(contact -> {
            Company company = contact.getCompany();
            return company == null ? "-" : company.getName();
        }).setHeader("Company");

        //column sizing to aouto
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }






}
