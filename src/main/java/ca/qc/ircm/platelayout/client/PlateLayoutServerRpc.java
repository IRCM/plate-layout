package ca.qc.ircm.platelayout.client;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.ServerRpc;

/**
 * Server RPC for plate layout.
 */
public interface PlateLayoutServerRpc extends ServerRpc {
  /**
   * Column header was clicked.
   *
   * @param column
   *          column index
   * @param mouseDetails
   *          mouse details
   */
  public void columnHeaderClicked(int column, MouseEventDetails mouseDetails);

  /**
   * Row header was clicked.
   *
   * @param row
   *          row index
   * @param mouseDetails
   *          mouse details
   */
  public void rowHeaderClicked(int row, MouseEventDetails mouseDetails);

  /**
   * Well was clicked.
   *
   * @param column
   *          column index
   * @param row
   *          row index
   * @param mouseDetails
   *          mouse details
   */
  public void wellClicked(int column, int row, MouseEventDetails mouseDetails);
}
