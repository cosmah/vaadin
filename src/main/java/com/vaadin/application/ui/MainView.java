package com.vaadin.application.ui;

import com.vaadin.application.backend.entity.Company;
import com.vaadin.application.backend.entity.Contact;
import com.vaadin.application.backend.service.CompanyService;
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
    public MainView(ContactService contactService, CompanyService companyservice) {
        this.contactService = contactService;
        addClassName("list-view");
        setSizeFull();
        configureFilter();
        configureGrid();

        //call form constructor
        form = new ContactForm(companyservice.findAll());

        //Creates a Div that wraps the grid and the form, gives it a CSS class name, and makes
        //it full size
        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();


        // Adds the content layout to the main layout
        add(filterText, content);
        updateList();

        //① The closeEditor() call at the end of the constructor Sets the form contact to null,
        //clearing out old values. Hides the form. Removes the "editing" CSS class from the
        //view.
        closeEditor();
    }

    //text field method
    private void configureFilter() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        //② addValueChangeListener adds a listener to the grid. The Grid component supports
        //multi and single-selection modes. We only want to select a single Contact, so we use
        //the asSingleSelect() method. The getValue() method returns the Contact in the
        //selected row or null if there’s no selection
        grid.asSingleSelect().addValueChangeListener(event ->
                editContact(event.getValue()));
    }

    //③ editContact sets the selected contact in the ContactForm and hides or shows the
    //form, depending on the selection. It also sets the "editing" CSS class name when
    //editing
    public void editContact(Contact contact) {
        if (contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }
    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
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
