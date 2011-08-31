package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InstanceInitializerInfo;


/**
 * �N���X�̃C���X�^���X�C�j�V�����C�U�̖���������ۑ�����N���X
 * 
 * @author t-miyake
 *
 */
public class UnresolvedInstanceInitializerInfo extends
        UnresolvedInitializerInfo<InstanceInitializerInfo> {

    /**
     * ���̃C���X�^���X�C�j�V�����C�U�����L����N���X��^���ď�����
     * @param ownerClass �C���X�^���X�C�j�V�����C�U�����L����N���X
     */
    public UnresolvedInstanceInitializerInfo(UnresolvedClassInfo ownerClass) {
        super(ownerClass);
    }

    /**
     * ���L�N���X��^���āC�I�u�W�F�N�g��������
     * 
     * @param ownerClass ���L�N���X
     */
    public UnresolvedInstanceInitializerInfo(final UnresolvedClassInfo ownerClass, int fromLine,
            int fromColumn, int toLine, int toColumn) {
        super(ownerClass, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    public boolean isInstanceMember() {
        return true;
    }

    @Override
    public boolean isStaticMember() {
        return false;
    }

    @Override
    protected InstanceInitializerInfo buildResolvedInfo(int fromLine, int fromColumn, int toLine,
            int toColumn) {
        return new InstanceInitializerInfo(fromLine, fromColumn, toLine, toColumn);
    }
}
