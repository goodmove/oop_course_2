package ru.nsu.ccfit.boltava.view.ProductionStatistics;

import ru.nsu.ccfit.boltava.model.FactoryManager;

import javax.swing.*;

public class ProductionStatisticsPanel extends JComponent {

    private ProductionInfoRow mCarsProducedInfoLabel;
    private ProductionInfoRow mEnginesSuppliedLabel;
    private ProductionInfoRow mBodiesSuppliedLabel;
    private ProductionInfoRow mAccessoriesSuppliedLabel;
    private JPanel mPanel;
    private JLabel mTitle;

    public ProductionStatisticsPanel(FactoryManager factoryManager) {
        mTitle.setText("Supplement Statistics");
        factoryManager.getCarStorageManager().addOnValueChangedListener(mCarsProducedInfoLabel);
        factoryManager.getBodyStorageManager().addOnValueChangedListener(mBodiesSuppliedLabel);
        factoryManager.getEngineStorageManager().addOnValueChangedListener(mEnginesSuppliedLabel);
        factoryManager.getAccessoryStorageManager().addOnValueChangedListener(mAccessoriesSuppliedLabel);
    }

    private void createUIComponents() {
        mCarsProducedInfoLabel = new ProductionInfoRow("Cars", "0");
        mEnginesSuppliedLabel = new ProductionInfoRow("Engines", "0");
        mBodiesSuppliedLabel = new ProductionInfoRow("Bodies", "0");
        mAccessoriesSuppliedLabel = new ProductionInfoRow("Accessories", "0");
    }

}
