package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������ for �u���b�N��\���N���X
 * 
 * @author higo
 */
public final class UnresolvedForBlockInfo extends UnresolvedConditionalBlockInfo<ForBlockInfo> {

    /**
     * �O���̃u���b�N����^���āCfor �u���b�N����������
     * 
     * @param outerSpace �O���̃u���b�N
     */
    public UnresolvedForBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);

        this.initializerExpressions = new HashSet<UnresolvedConditionInfo<? extends ConditionInfo>>();
        this.iteratorExpressions = new HashSet<UnresolvedExpressionInfo<? extends ExpressionInfo>>();
    }

    /**
     * ���̖����� for �u���b�N����������
     * 
     * @param usingClass �����N���X
     * @param usingMethod �������\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     */
    @Override
    public ForBlockInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // ���� for���̈ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new ForBlockInfo(fromLine, fromColumn, toLine, toColumn);

        final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace = this.getOuterSpace();
        final LocalSpaceInfo outerSpace = unresolvedLocalSpace.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOuterUnit(outerSpace);

        return this.resolvedInfo;
    }

    /**
     * ���̃��[�J���̈�̃C���i�[�̈�𖼑O��������
     * 
     * @param usingClass ���̗̈悪���݂��Ă���N���X
     * @param usingMethod ���̗̈悪���݂��Ă��郁�\�b�h
     * @param classInfoManager �N���X�}�l�[�W��
     * @param fieldInfoManager �t�B�[���h�}�l�[�W��
     * @param methodInfoManager ���\�b�h�}�l�[�W��
     */
    @Override
    public final void resolveInnerBlock(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        super.resolveInnerBlock(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);

        // �������������������������C�����ς݃I�u�W�F�N�g�ɒǉ�
        for (final UnresolvedConditionInfo<? extends ConditionInfo> unresolvedInitializer : this.initializerExpressions) {
            final ConditionInfo initializer = unresolvedInitializer.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addInitializerExpressions(initializer);
        }

        // �������X�V������ǉ����C�����ς݃I�u�W�F�N�g�ɒǉ�
        for (final UnresolvedExpressionInfo<? extends ExpressionInfo> unresolvedUpdater : this.iteratorExpressions) {
            final ExpressionInfo update = unresolvedUpdater.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addIteratorExpressions(update);
        }
    }

    /**
     * ����������ǉ����郁�\�b�h
     * 
     * @param initializerExpression �ǉ����鏉������
     */
    public final void addInitializerExpression(
            final UnresolvedConditionInfo<? extends ConditionInfo> initializerExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == initializerExpression) {
            throw new IllegalArgumentException("initailizerExpression is null.");
        }

        this.initializerExpressions.add(initializerExpression);
    }

    /**
     * �X�V����ǉ����郁�\�b�h
     * 
     * @param iteratorExpression �ǉ�����X�V��
     */
    public final void addIteratorExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> iteratorExpression) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == iteratorExpression) {
            throw new IllegalArgumentException("updateExpression is null.");
        }

        this.iteratorExpressions.add(iteratorExpression);
    }

    public Set<UnresolvedConditionInfo<? extends ConditionInfo>> getInitializerExpressions() {
        return Collections.unmodifiableSet(this.initializerExpressions);
    }

    public Set<UnresolvedExpressionInfo<? extends ExpressionInfo>> getIteratorExpressions() {
        return Collections.unmodifiableSet(this.iteratorExpressions);
    }

    private final Set<UnresolvedConditionInfo<? extends ConditionInfo>> initializerExpressions;

    private final Set<UnresolvedExpressionInfo<? extends ExpressionInfo>> iteratorExpressions;

}
