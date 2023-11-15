package com.vaadin.application.ui;

import java.util.List;

import com.vaadin.application.backend.entity.Company;
import com.vaadin.application.backend.entity.Contact;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class ContactForm extends FormLayout {
    private Contact contact;

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    EmailField email = new EmailField("Email");
    ComboBox<Contact.Status> status = new ComboBox<>("Status");
    ComboBox<Company> company = new ComboBox<>("Company");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    //creating a binderthat is aware of bean validation annotations(beanvalidationbinder)
    //by passing it in Contact.class we define the object we're binding to
    Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);



    public ContactForm(List<Company> companies) {
        //bindInstanceFields matches fields in Contact and ContactForm base on names

            addClassName("contact-form");
            binder.bindInstanceFields(this);
            company.setItems(companies);
            company.setItemLabelGenerator(Company::getName);
            status.setItems(Contact.Status.values());


            //form structure
        addClassName("contact-form");
        add(firstName,
                lastName,
                email,
                company,
                status,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);
        return new HorizontalLayout(save, delete, close);



    }

    public void setContact(Contact contact){
        //Save a reference to the contact so we can save the form values back into it later.
        this.contact = contact;
        //Calls binder.readBean to bind the values from the contact to the UI fields. readBen
        //copies the values from the Contact to an internal model, that way we don’t
        //accidentally overwrite values if we cancel editing
        binder.readBean(contact);
    }

    // Events
    public static abstract class ContactFormEvent extends ComponentEvent<ContactForm> {
        private Contact contact;
        //ContactFormEvent is a common superclass for all the events. It contains the contact
        //that was edited or deleted
        protected ContactFormEvent(ContactForm source, Contact contact) {
            super(source, false);
            this.contact = contact;
        }
        public Contact getContact() {
            return contact;
        }
    }
    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }
    }
    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }
    }
    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(ContactForm source) {
            super(source, null);
        }
    }
    // The addListener method uses Vaadin’s event bus to register the custom event types
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    //saving, deleting, and closing the form
    private Component createButtonsLayout() {
        //  The save button calls the validateAndSave method
        save.addClickListener(event -> validateAndSave());
        //② The delete button fires a delete event and passes the currently-edited contact
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, contact)));
        //③ The cancel button fires a close event.
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        //④ Validates the form every time it changes. If it is invalid, it disables the save button to
        //avoid invalid submissions
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }
    private void validateAndSave() {
        try {
            //⑤ Write the form contents back to the original contact.
            binder.writeBean(contact);
            //⑥ Fire a save event so the parent component can handle the action.
            fireEvent(new SaveEvent(this, contact));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

}