package com.despegar.p13n.hestia.recommend.allinone;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.google.common.collect.Lists;

@Service
public class LastResortProfilService
    implements ProfilesService {

    private ConcurrentMap<UserProfile, Usage> lastResortProfiles = new ConcurrentHashMap<UserProfile, Usage>();

    @Override
    public void registerProfile(HomeParam homeParam, Product homeToCheck, RulesVersion version, boolean isLastResort,
        ActivityType activity) {

        UserProfile profile = new UserProfile(homeParam.getCc(), version, homeToCheck, activity);
        this.registerProfile(profile, isLastResort);
    }


    private void registerProfile(UserProfile profile, boolean isLastResort) {

        if (this.lastResortProfiles.containsKey(profile)) {
            if (isLastResort) {
                this.lastResortProfiles.get(profile).incEmpty();
            } else {
                this.lastResortProfiles.get(profile).incFull();
            }
        } else {
            Usage usage = new Usage();
            if (isLastResort) {
                usage.incEmpty();
            } else {
                usage.incFull();
            }
            this.lastResortProfiles.put(profile, usage);
        }
    }

    @Override
    public List<String> dumpLastResortProfiles() {

        List<LastResortProfile> profiles = Lists.newArrayList();


        synchronized (this.lastResortProfiles) {
            for (Entry<UserProfile, Usage> entry : this.lastResortProfiles.entrySet()) {

                CountryCode cc = entry.getKey().getCc();
                RulesVersion rule = entry.getKey().getRule();
                Product product = entry.getKey().getProduct();
                ActivityType activity = entry.getKey().getActivity();
                UserProfile userProfile = new UserProfile(cc, rule, product, activity);
                int empty = entry.getValue().getEmpty().intValue();
                int full = entry.getValue().getFull().intValue();
                int call = empty + full;
                float rate = call == 0 ? 0 : (empty * 100) / call;
                LastResortProfile profile = new LastResortProfile(userProfile, full, empty, rate, activity);
                profiles.add(profile);
            }
        }
        Collections.sort(profiles, new LastResortProfile());
        final String format = "%-10s   " + "%-15s   " + "%-15s   " + "%-15s   " + "%-5s  (%-9s/%-9s) ";
        String header = String.format(format, "CountryCode", "RuleVersion", "ActivityType", "Home", "LastResort Rate (%)",
            "FullContent", "EmptyContent");
        List<String> stringProfiles = Lists.newArrayList();
        stringProfiles.add(header);
        for (LastResortProfile prof : profiles) {
            stringProfiles.add(String.format(format, prof.getUserProfile().getCc(), prof.getUserProfile().getRule(),
                prof.getActivity(), prof.getUserProfile().getProduct(), prof.getLastResortRate(), prof.getFullContent(),
                prof.getEmptyContent()));
        }
        return stringProfiles;
    }
}
