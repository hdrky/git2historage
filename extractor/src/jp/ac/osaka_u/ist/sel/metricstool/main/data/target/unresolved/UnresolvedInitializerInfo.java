package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������C�j�V�����C�U�̋��ʂ̐e�N���X
 * <br>
 * �C�j�V�����C�U�Ƃ́C�X�^�e�B�b�N�E�C�j�V�����C�U��C���X�^���X�E�C�j�V�����C�U�@�Ȃǂł��� 
 * 
 * @param <T> �����ς݃C�j�V�����C�U���̌^
 * @author�@g-yamada
 *
 */
public abstract class UnresolvedInitializerInfo<T extends InitializerInfo> extends
        UnresolvedCallableUnitInfo<T> {

    /**
     * ���L�N���X��^���āC�I�u�W�F�N�g��������
     * 
     * @param ownerClass ���L�N���X
     */
    public UnresolvedInitializerInfo(final UnresolvedClassInfo ownerClass) {
        super(ownerClass);
    }

    /**
     * ���L�N���X��^���āC�I�u�W�F�N�g��������
     * 
     * @param ownerClass ���L�N���X
     */
    public UnresolvedInitializerInfo(final UnresolvedClassInfo ownerClass, int fromLine,
            int fromColumn, int toLine, int toColumn) {
        super(ownerClass, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * ���O�������s��
     */
    @Override
    public T resolve(final TargetClassInfo usingClass, final CallableUnitInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final UnresolvedClassInfo unresolvedOwnerClass = this.getOwnerClass();
        final TargetClassInfo ownerClass = unresolvedOwnerClass.resolve(null, null,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo = this.buildResolvedInfo(this.getFromLine(), this.getFromColumn(), this
                .getToLine(), this.getToColumn());
        this.resolvedInfo.setOuterUnit(ownerClass);

        return this.resolvedInfo;
    }

    protected abstract T buildResolvedInfo(final int fromLine, final int fromColumn,
            final int toLine, final int toColumn);
}
