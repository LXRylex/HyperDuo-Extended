package io.github.yixing233.hyperduo;
import java.util.*;
public class StatusBarTintTest {
    static int checks;
    static void check(boolean ok,String why){checks++;if(!ok)throw new AssertionError(why);}
    public interface Receiver {
        void onDarkChanged(ArrayList<?> areas,float intensity,int tint);
        void onDarkChangedWithContrast(ArrayList<?> areas,int tint,int contrast);
        void onLightDarkTintChanged(int light,int dark,boolean useTint);
    }
    public static class NativeReceiver implements Receiver {
        ArrayList<?> areas;int tint,light,dark,contrast;boolean useTint;int calls;
        public void onDarkChanged(ArrayList<?> areas,float intensity,int tint){this.areas=areas;this.tint=tint;calls++;}
        public void onDarkChangedWithContrast(ArrayList<?> areas,int tint,int contrast){this.areas=areas;this.tint=tint;this.contrast=contrast;}
        public void onLightDarkTintChanged(int light,int dark,boolean useTint){this.light=light;this.dark=dark;this.useTint=useTint;}
    }
    public static void main(String[] args){
        TrioSettings s=TrioSettings.defaults();s.trioStyle=Prefs.STYLE_HEART;s.enabled=true;s.heartStatusTint=true;
        check(StatusBarTintPolicy.enabled(s,false),"Heart may match the status bar");
        s.trioStyle=Prefs.STYLE_DEFAULT;check(!StatusBarTintPolicy.enabled(s,false),"Default restores native tint");
        s.trioStyle=Prefs.STYLE_HEART;s.enabled=false;check(!StatusBarTintPolicy.enabled(s,false),"master off restores native tint");
        s.enabled=true;s.heartStatusTint=false;check(!StatusBarTintPolicy.enabled(s,false),"theme toggle off restores native tint");
        s.heartStatusTint=true;check(!StatusBarTintPolicy.enabled(s,true),"Heart failure also releases native tint");
        check(s.copy().heartStatusTint && TrioSettings.fromBundle(s.toBundle()).heartStatusTint,"theme preference survives copy/broadcast");
        TrioSettings off=s.copy();off.heartStatusTint=false;
        check(!s.equals(off) && s.applyKeyFrom(off,Prefs.KEY_HEART_STATUS_TINT) && !s.heartStatusTint,"theme preference updates live");
        s.heartNormal=0xff93bbfa;s.heartCharging=0xfffcb8e2;
        check(StatusBarTintPolicy.color(s,48,false)==0xff93bbfa,"normal color matches the chosen Heart fill");
        check(StatusBarTintPolicy.color(s,48,true)==0xfffcb8e2,"charging color follows battery state");
        check(StatusBarTintPolicy.color(s,-1,false)==0xff93bbfa,"unknown battery level keeps normal color");
        check(StatusBarTintPolicy.isStatusBarRoot("com.android.systemui.statusbar.phone.MiuiPhoneStatusBarView"),"Xiaomi status bar is included");
        check(!StatusBarTintPolicy.isStatusBarRoot("com.android.systemui.controlcenter.phone.ControlCenterPanelView"),"control center content is excluded");
        check(StatusBarTintPolicy.protectedElement("privacy_dot") && StatusBarTintPolicy.protectedElement("ongoing_call_chip"),"privacy/call indicators retain semantic colors");
        check(StatusBarTintPolicy.isStatusBarRoot("com.android.systemui.statusbar.phone.MiuiKeyguardStatusBarView"),"HyperOS lock status bar is included");
        check(StatusBarTintPolicy.isStatusBarRoot("com.android.systemui.statusbar.phone.KeyguardStatusBarView"),"base lock status bar is included");
        check(!StatusBarTintPolicy.isStatusBarRoot("com.android.keyguard.clock.KeyguardClockContainer"),"large lock clock is excluded");
        check(StatusBarTintPolicy.isShadeHeaderRoot("com.android.systemui.qs.MiuiNotificationHeaderView"),"notification header follows selected color");
        check(StatusBarTintPolicy.isShadeHeaderRoot("com.android.systemui.controlcenter.shade.ControlCenterHeaderView"),"control center header follows selected color");
        check(StatusBarTintPolicy.isStatusBarRoot("com.android.systemui.controlcenter.phone.widget.ControlCenterStatusBarIcon"),"expanded native icon group is included");
        check(StatusBarTintPolicy.isStatusBarRoot("com.android.systemui.controlcenter.phone.widget.ControlCenterFakeStatusIcons"),"transition native icon group is included");
        check(!StatusBarTintPolicy.isShadeHeaderRoot("com.android.systemui.statusbar.notification.row.ExpandableNotificationRow"),"notification card content stays native");
        check(!StatusBarTintPolicy.isShadeHeaderRoot("com.android.systemui.controlcenter.phone.ControlCenterPanelView"),"tiles and panel content stay native");
        StatusBarTintOverlay<Integer> overlay=new StatusBarTintOverlay<>();Object clock=new Object();
        overlay.remember(clock,0xffffffff);overlay.remember(clock,0xff93bbfa);
        check(overlay.release(clock)==0xffffffff && !overlay.contains(clock),"repeated tinting keeps original native white for restoration");
        overlay.remember(clock,0xffffffff);overlay.nativeUpdated(clock);overlay.remember(clock,0xff000000);
        check(overlay.release(clock)==0xff000000,"native theme updates replace stale baselines");
        overlay.remember(clock,0xffffffff);check(overlay.release(clock)==0xffffffff,"equal native and chosen white still restores");
        StatusBarTintOverlay<Object> filter=new StatusBarTintOverlay<>();filter.remember(clock,null);
        check(filter.contains(clock) && filter.release(clock)==null && !filter.contains(clock),"an originally absent image filter restores to null");
        NativeReceiver nativeReceiver=new NativeReceiver();boolean[] enabled={false};List<Throwable> errors=new ArrayList<>();
        Receiver proxy=(Receiver)StatusBarTintProxy.wrap(Receiver.class,nativeReceiver,()->enabled[0],()->0xff93bbfa,errors::add);
        ArrayList<String> originalAreas=new ArrayList<>();originalAreas.add("native partial tint area");
        proxy.onDarkChanged(originalAreas,.4f,0xffffffff);
        check(nativeReceiver.tint==0xffffffff && nativeReceiver.areas==originalAreas,"out-of-scope/off callbacks remain native");
        enabled[0]=true;proxy.onDarkChanged(originalAreas,.4f,0xffffffff);
        check(nativeReceiver.tint==0xff93bbfa && nativeReceiver.areas.isEmpty(),"all eligible status icons receive selected tint regardless of partial dark area");
        check(originalAreas.size()==1,"native tint regions are never mutated");
        proxy.onDarkChangedWithContrast(originalAreas,0xffffffff,0xff000000);
        check(nativeReceiver.tint==0xff93bbfa && nativeReceiver.contrast==0xff000000,"contrast outline is preserved");
        proxy.onLightDarkTintChanged(0xffffffff,0xff000000,true);
        check(nativeReceiver.light==0xff93bbfa && nativeReceiver.dark==0xff93bbfa && nativeReceiver.useTint,"both Xiaomi tint paths match chosen color");
        enabled[0]=false;proxy.onDarkChanged(originalAreas,.4f,0xff000000);
        proxy.onLightDarkTintChanged(0xffffffff,0xff000000,false);
        check(nativeReceiver.tint==0xff000000 && nativeReceiver.light==0xffffffff && nativeReceiver.dark==0xff000000,"disable replays original native light/dark colors");
        Map<Object,Object> map=new IdentityHashMap<>();map.put(nativeReceiver,proxy);
        check(map.remove(nativeReceiver)==proxy,"native receiver key/removal is unchanged");
        check(proxy.equals(proxy) && !proxy.equals(nativeReceiver),"proxy identity methods remain stable");
        check(errors.isEmpty(),"normal receiver forwarding has no exceptions");
        NativeReceiver refusing=new NativeReceiver(){public void onDarkChanged(ArrayList<?> a,float i,int c){if(c==0xff93bbfa)throw new IllegalArgumentException("native receiver rejects custom tint");super.onDarkChanged(a,i,c);}};
        Receiver safe=(Receiver)StatusBarTintProxy.wrap(Receiver.class,refusing,()->true,()->0xff93bbfa,errors::add);
        safe.onDarkChanged(originalAreas,0f,0xffffffff);
        check(refusing.tint==0xffffffff && !errors.isEmpty(),"a custom-tint failure falls back to native arguments");
        System.out.println("PASS: "+checks+" native status-bar color/restore checks");
    }
}
