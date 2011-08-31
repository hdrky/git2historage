package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �C���X�^���X�C�j�V�����C�U��\���N���X
 * 
 * @author t-miyake
 *
 */
public class InstanceInitializerInfo extends InitializerInfo {

    /**
     * 
     */
    private static final long serialVersionUID = 5833181372993442712L;

    /**
     * �I�u�W�F�N�g��������
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public InstanceInitializerInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
    }
}
