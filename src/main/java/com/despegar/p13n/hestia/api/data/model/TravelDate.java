package com.despegar.p13n.hestia.api.data.model;

import org.joda.time.LocalDate;

public class TravelDate {

    private LocalDate startDate;
    private LocalDate endDate;

    public TravelDate(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public boolean overlapsWith(TravelDate other) {
        return (this.startDate.isBefore(other.endDate) && other.startDate.isBefore(this.endDate));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.endDate == null) ? 0 : this.endDate.hashCode());
        result = prime * result + ((this.startDate == null) ? 0 : this.startDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        TravelDate other = (TravelDate) obj;
        if (this.endDate == null) {
            if (other.endDate != null) {
                return false;
            }
        } else if (!this.endDate.equals(other.endDate)) {
            return false;
        }
        if (this.startDate == null) {
            if (other.startDate != null) {
                return false;
            }
        } else if (!this.startDate.equals(other.startDate)) {
            return false;
        }
        return true;
    }

    public static TravelDate expandedTravelDate(TravelDate one, TravelDate two) {
        LocalDate minStartDate = one.startDate.isBefore(two.startDate) ? one.startDate : two.startDate;
        LocalDate maxEndDate = one.endDate.isAfter(two.endDate) ? one.endDate : two.endDate;
        return new TravelDate(minStartDate, maxEndDate);
    }

}
