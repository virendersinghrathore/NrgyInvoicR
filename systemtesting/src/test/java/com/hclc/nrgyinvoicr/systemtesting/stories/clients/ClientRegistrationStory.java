package com.hclc.nrgyinvoicr.systemtesting.stories.clients;

import com.hclc.nrgyinvoicr.systemtesting.Application;
import com.hclc.nrgyinvoicr.systemtesting.stories.meters.Meter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.hclc.nrgyinvoicr.systemtesting.stories.clients.ClientBuilder.aClient;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

public class ClientRegistrationStory extends ClientStories {

    public ClientRegistrationStory(Application app) {
        super(app);
    }

    public Client userRegistersANewClient(Meter meter) {
        navigateToClientsPage();
        openClientRegistrationForm();
        return registerANewClient(meter);
    }

    private void openClientRegistrationForm() {
        app.findElement(By.id("ae-button-register-client")).click();
    }

    private Client registerANewClient(Meter meter) {
        Client client = aClient().withMeterSerialNumber(meter.serialNumber).build();
        app.findElement(By.id("ae-input-client-first-name")).sendKeys(client.fistName);
        app.findElement(By.id("ae-input-client-middle-name")).sendKeys(client.middleName);
        app.findElement(By.id("ae-input-client-last-name")).sendKeys(client.lastName);
        app.findElement(By.id("ae-input-client-address-line-1")).sendKeys(client.addressLine1);
        app.findElement(By.id("ae-input-client-address-line-2")).sendKeys(client.addressLine2);
        app.findElement(By.id("ae-input-client-postal-code")).sendKeys(client.postalCode);
        app.findElement(By.id("ae-input-client-city")).sendKeys(client.city);
        app.findElement(By.id("ae-input-meter-autocomplete")).sendKeys(client.meterSerialNumber);
        app.clickWith1sTimeout(By.xpath("//mat-option/span[text()=' " + client.meterSerialNumber + " ']/.."));
        app.findElement(By.id("ae-button-client-save")).click();
        return client;
    }

    public void assertThatUserSeesARegisteredClientInAListOfClients(Client client) {
        assertThatCode(() -> {
            WebElement clientRow = app.findElement(By.xpath("//*[@id='ae-table-clients']/mat-row/mat-cell[@id='ae-cell-client-last-name' and text()=' " + client.lastName + " ']/.."));
            clientRow.findElement(By.xpath("mat-cell[@id='ae-cell-client-first-name' and text()=' " + client.fistName + " ']"));
            clientRow.findElement(By.xpath("mat-cell[@id='ae-cell-client-address-line-1' and text()=' " + client.addressLine1 + " ']"));
            clientRow.findElement(By.xpath("mat-cell[@id='ae-cell-client-city' and text()=' " + client.city + " ']"));
            clientRow.findElement(By.xpath("mat-cell[@id='ae-cell-client-postal-code' and text()=' " + client.postalCode + " ']"));
            clientRow.findElement(By.xpath("mat-cell[@id='ae-cell-client-meter-serial-number' and text()=' " + client.meterSerialNumber + " ']"));
        }).doesNotThrowAnyException();
    }

    public void assertThatClientRegistrationFormShowsAllValuesForA(Client client) {
        app.hoverOverElement(By.xpath("//*[@id='ae-table-clients']/mat-row/mat-cell[text()=' " + client.lastName + " ']/.."));
        app.clickWith1sTimeout(By.id("ae-button-edit-client"));
        assertThat(app.getValueOfElement(By.id("ae-input-client-first-name"))).isEqualTo(client.fistName);
        assertThat(app.getValueOfElement(By.id("ae-input-client-middle-name"))).isEqualTo(client.middleName);
        assertThat(app.getValueOfElement(By.id("ae-input-client-last-name"))).isEqualTo(client.lastName);
        assertThat(app.getValueOfElement(By.id("ae-input-client-address-line-1"))).isEqualTo(client.addressLine1);
        assertThat(app.getValueOfElement(By.id("ae-input-client-address-line-2"))).isEqualTo(client.addressLine2);
        assertThat(app.getValueOfElement(By.id("ae-input-client-postal-code"))).isEqualTo(client.postalCode);
        assertThat(app.getValueOfElement(By.id("ae-input-client-city"))).isEqualTo(client.city);
        assertThat(app.getValueOfElement(By.id("ae-input-meter-autocomplete"))).isEqualTo(client.meterSerialNumber);
        app.findElement(By.id("ae-button-client-cancel")).click();
    }
}
