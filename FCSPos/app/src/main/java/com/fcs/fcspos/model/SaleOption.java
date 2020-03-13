package com.fcs.fcspos.model;

public interface SaleOption {
    void optionSaleKind(int selectedOption);
    void optionProductKind(int selectedProduct);
    void optionVehicleKind(int selectedVehicle);
    void optionPresetKind(int selectedKindPreset);
    void identificationKind(Identification identification);
    void mileage(String quantity);
    void authorizedCustomer(Client client);
    void showCustomerInformation(Client client);
    void money(int money);
    void volume(double volume);
    void correctHose(boolean is_hose);
    void positionChange();
    void endSale(Sale sale);
    void receipt();


}
