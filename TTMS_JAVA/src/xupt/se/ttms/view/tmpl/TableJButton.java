package xupt.se.ttms.view.tmpl;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class TableJButton extends JButton implements TableCellRenderer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public TableJButton(String string) {
        this.setText(string);
    }

    public TableJButton() {
        // TODO 自动生成的构造函数存根
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        // TODO 自动生成的方法存根
        return null;
    }

}
