package org.fossasia.openevent.app.common.app.di.component;

import org.fossasia.openevent.app.OrgaApplication;
import org.fossasia.openevent.app.common.app.di.module.DataModule;
import org.fossasia.openevent.app.common.app.di.module.NetworkModule;
import org.fossasia.openevent.app.common.app.di.module.PresenterModule;
import org.fossasia.openevent.app.module.attendee.checkin.AttendeeCheckInFragment;
import org.fossasia.openevent.app.module.attendee.list.AttendeesFragment;
import org.fossasia.openevent.app.module.event.chart.ChartActivity;
import org.fossasia.openevent.app.module.event.dashboard.EventDashboardFragment;
import org.fossasia.openevent.app.module.event.list.EventListFragment;
import org.fossasia.openevent.app.module.login.LoginActivity;
import org.fossasia.openevent.app.module.main.MainActivity;
import org.fossasia.openevent.app.module.settings.SettingsFragment;
import org.fossasia.openevent.app.module.tickets.TicketsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    DataModule.class,
    NetworkModule.class,
    PresenterModule.class
})
public interface AppComponent {

    void inject(LoginActivity loginActivity);

    void inject(MainActivity mainActivity);

    void inject(EventListFragment eventListFragment);

    void inject(EventDashboardFragment eventDashboardFragment);

    void inject(AttendeesFragment attendeesFragment);

    void inject(AttendeeCheckInFragment attendeeCheckInFragment);

    void inject(OrgaApplication orgaApplication);

    void inject(SettingsFragment settingsFragment);

    void inject(ChartActivity chartActivity);

    void inject(TicketsFragment ticketsFragment);

}
