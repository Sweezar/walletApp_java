package org.berneick;

import org.berneick.controller.WalletControllerTest;
import org.berneick.service.WalletService;
import org.berneick.service.WalletServiceTest;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class TestRunner {
    public static void main(String[] args) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(
                        selectClass(WalletServiceTest.class),
                        selectClass(WalletControllerTest.class)
                )
                .build();

        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();
        System.out.println("Test run: " + summary.getTestsFoundCount());
        System.out.println("Test success: " + summary.getTestsSucceededCount());
        System.out.println("Test failed: " + summary.getTestsFailedCount());
    }
}
