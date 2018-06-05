package pl.jakubpradzynski.crispus.components;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * A class that is a Spring component that allows you to manage local time.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TimeComponent {

    /**
     * The time is set to the current one with each request.
     */
    private LocalDateTime time = LocalDateTime.now();

    /**
     * @return local data time.
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * Set new local data time from parameter.
     * @param time - new local data time.
     */
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

}
