package com.despegar.p13n.hestia.recommend.allinone;

import java.net.InetAddress;
import java.util.EnumSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HomeContentTrace;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.google.common.base.Preconditions;

public class HomeParam {

	private final CountryCode cc;
    private final InetAddress ip;
    private final EnumSet<Product> home;
    private final String userId;
    private final Language lan;
    private final Queue<String> debug;
    private final HomeContentTrace trace;
    private final Long toTs;
    private final RulesVersion forceRulesVersion;
    private final MultiObjecHomeVersion forceMOVersion;
    private volatile boolean timeouted;


    /**
     * For debugging use
     */
    public HomeParam(CountryCode cc, InetAddress ip, String userId, Language lan, boolean debugEnabled) {
        this(cc, ip, null, userId, lan, debugEnabled, false, null, null, null);
    }

    public HomeParam(CountryCode cc, Language language, MultiObjecHomeVersion homeVesion) {
        this(cc, null, null, null, language, false, false, null, null, homeVesion);
    }

    public HomeParam(CountryCode cc, InetAddress ip, Product home, String userId, Language lan) {
        this(cc, ip, EnumSet.of(home), userId, lan, false, false, null, null, null);
    }


    public HomeParam(CountryCode cc, InetAddress ip, EnumSet<Product> homes, String userId, Language lan,
        boolean debugEnabled, boolean traceEnabled, Long toTs, RulesVersion forceRulesVersion,
        MultiObjecHomeVersion forceHomeVersion) {

        Preconditions.checkNotNull(cc);
        Preconditions.checkNotNull(lan);

        this.cc = cc;
        this.ip = ip;
        this.home = homes;
        this.userId = userId;
        this.lan = lan;

        this.debug = debugEnabled ? new ConcurrentLinkedQueue<String>() : null;
        this.trace = traceEnabled ? new HomeContentTrace() : null;
        this.toTs = toTs;
        this.forceRulesVersion = forceRulesVersion;
        this.forceMOVersion = forceHomeVersion;
    }


    public CountryCode getCc() {
        return this.cc;
    }

    public InetAddress getIp() {
        return this.ip;
    }

    public EnumSet<Product> getHome() {
        return this.home;
    }

    public String getUserId() {
        return this.userId;
    }

    public Language getLan() {
        return this.lan;
    }

    public Queue<String> getDebug() {
        return this.debug;
    }

    public HomeContentTrace getTrace() {
        return this.trace;
    }

    public Long getToTs() {
        return this.toTs;
    }

    public RulesVersion getForceRulesVersion() {
        return this.forceRulesVersion;
    }

    public MultiObjecHomeVersion getForceMOVersion() {
        return this.forceMOVersion;
    }


    public void setTimeOutTrace(boolean timeOut) {
        if (this.trace != null) {
            this.trace.setTimeOut(timeOut);
        }

    }

    public void addLastResortTrace(Product homeToCheck) {
        if (this.trace != null) {
            this.trace.getLastResort().add(homeToCheck);
        }
    }

    public void setErrorTrace(boolean isError) {
        if (this.trace != null) {
            this.trace.setError(isError);
        }
    }

    public void debug(String str) {

        if (this.debug != null && !this.timeouted) {
            // no printing debug after timeouted, so we exclude the debug for a task that has timeouted but it is
            // running

            // print current thread name
            String msg = String.format("[%s]-%s", Thread.currentThread().getName().substring(0, 4), str);
            this.debug.add(msg);
        }
    }

    public boolean isTimeouted() {
        return this.timeouted;
    }

    public void setTimeouted(boolean timeouted) {
        this.timeouted = timeouted;
    }

    @Override
    public String toString() {
        return "HomeParam [cc=" + this.cc + ", ip=" + this.ip + ", home=" + this.home + ", userId=" + this.userId + ", lan="
            + this.lan + ", debug=" + this.debug + ", trace=" + this.trace + ", toTs=" + this.toTs + ", forceRulesVersion="
            + this.forceRulesVersion + ", forceMOVersion=" + this.forceMOVersion + "]";
    }
}
