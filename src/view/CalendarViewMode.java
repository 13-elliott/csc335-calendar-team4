package view;

import javafx.scene.Node;

import java.time.LocalDate;

public interface CalendarViewMode {

    /**
     * get the parent node for this view mode
     *
     * @return the parent node for this view mode
     */
    Node getNode();

    /**
     * Change the range of time being shown by this view to
     * a range of time which include the given DateTime.
     *
     * @param date a point in time which this
     */
    void setDate(LocalDate date);

    /**
     * get the first date within the range of time being shown
     * @return the first date within the range of time being shown
     */
    LocalDate getDate();
}
