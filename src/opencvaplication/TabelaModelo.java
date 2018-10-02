package opencvaplication;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Leonardo
 */
public class TabelaModelo extends AbstractTableModel {

    private ArrayList linhas = null;
    private String[] colunas = null;

    public TabelaModelo(ArrayList lin, String[] col) {
        setLinhas(lin);
        setColunas(col);
    }

    public ArrayList getLinhas() {
        return linhas;

    }

    public void setLinhas(ArrayList dados) {
        linhas = dados;
    }

    public String[] getColunas() {
        return colunas;
    }

    public void setColunas(String[] nomes) {
        colunas = nomes;
    }

    @Override
    public String getColumnName(int numCol) {
        return colunas[numCol];
    }

    @Override
    public int getRowCount() {
        return linhas.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int numLin, int numCol) {
        Object[] linha = (Object[])getLinhas().get(numLin);
        return linha[numCol];
    }

}
