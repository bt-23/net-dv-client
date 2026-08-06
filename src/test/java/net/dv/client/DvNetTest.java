package net.dv.client;

import net.dv.client.core.net.HttpTransport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DvNetTest {

    @Test
    void exposesEveryService() {
        try (DvNet dvNet = new DvNet(mock(HttpTransport.class))) {
            assertNotNull(dvNet.currencies());
            assertNotNull(dvNet.wallets());
            assertNotNull(dvNet.balances());
            assertNotNull(dvNet.withdrawals());
            assertNotNull(dvNet.transactions());
            assertNotNull(dvNet.webhooks());
        }
    }

    @Test
    void reusesTheSameServiceInstances() {
        try (DvNet dvNet = new DvNet(mock(HttpTransport.class))) {
            assertSame(dvNet.currencies(), dvNet.currencies());
            assertSame(dvNet.wallets(), dvNet.wallets());
        }
    }

    @Test
    void sharesOneTransportAcrossEveryService() {
        HttpTransport transport = mock(HttpTransport.class);

        try (DvNet dvNet = new DvNet(transport)) {
            assertSame(transport, dvNet.getTransport());
            assertSame(transport, dvNet.currencies().getTransport());
            assertSame(transport, dvNet.wallets().getTransport());
            assertSame(transport, dvNet.balances().getTransport());
            assertSame(transport, dvNet.withdrawals().getTransport());
            assertSame(transport, dvNet.transactions().getTransport());
        }
    }

    @Test
    void closingTheClientClosesTheTransport() {
        HttpTransport transport = mock(HttpTransport.class);

        new DvNet(transport).close();

        verify(transport).close();
    }

    @Test
    void rejectsAMissingTransport() {
        assertThrows(NullPointerException.class, () -> new DvNet((HttpTransport) null));
    }
}
