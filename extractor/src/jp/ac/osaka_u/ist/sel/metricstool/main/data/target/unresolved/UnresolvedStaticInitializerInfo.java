package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticInitializerInfo;


/**
 * ������ static initializer ��\���N���X
 * 
 * @author t-miyake, higo
 */
public class UnresolvedStaticInitializerInfo extends
        UnresolvedInitializerInfo<StaticInitializerInfo> {

    /**
     * ���L�N���X��^���āC�I�u�W�F�N�g��������
     * 
     * @param ownerClass ���L�N���X
     */
    public UnresolvedStaticInitializerInfo(final UnresolvedClassInfo ownerClass) {
        super(ownerClass);
    }

    /**
     * ���L�N���X��^���āC�I�u�W�F�N�g��������
     * 
     * @param ownerClass ���L�N���X
     */
    public UnresolvedStaticInitializerInfo(final UnresolvedClassInfo ownerClass, int fromLine,
            int fromColumn, int toLine, int toColumn) {
        super(ownerClass, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    public boolean isStaticMember() {
        return true;
    }

    @Override
    public boolean isInstanceMember() {
        return true;
    }

    @Override
    protected StaticInitializerInfo buildResolvedInfo(int fromLine, int fromColumn, int toLine,
            int toColumn) {
        return new StaticInitializerInfo(fromLine, fromColumn, toLine, toColumn);
    }
}
