package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TernaryOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �O�����Z�g�p�̖���������\���N���X
 * 
 * @author t-miyake
 *
 */
public class UnresolvedTernaryOperationInfo extends UnresolvedExpressionInfo<TernaryOperationInfo> {

    /**
     * �I�u�W�F�N�g��������
     * 
     * @param condition ������
     * @param trueExpression true�̏ꍇ�̎�
     * @param falseExpression false�̏ꍇ�̎�
     */
    public UnresolvedTernaryOperationInfo(
            final UnresolvedConditionInfo<? extends ConditionInfo> condition,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> trueExpression,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> falseExpression) {
        super();
        if (null == condition || null == trueExpression || null == falseExpression) {
            throw new IllegalArgumentException();
        }

        this.condition = condition;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
    }

    @Override
    public TernaryOperationInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {
        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ConditionInfo conditionalExpression = this.condition.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final ExpressionInfo trueExpression = this.trueExpression.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final ExpressionInfo falseExpression = this.falseExpression.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        /*// �v�f�g�p�̃I�[�i�[�v�f��Ԃ�
        final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                .getOwnerExecutableElement();
        final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);*/

        this.resolvedInfo = new TernaryOperationInfo(conditionalExpression, trueExpression,
                falseExpression, usingMethod, fromLine, fromColumn, toLine, toColumn);
        /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
        return this.resolvedInfo;
    }

    public UnresolvedConditionInfo<? extends ConditionInfo> getCondition() {
        return this.condition;
    }

    public UnresolvedExpressionInfo<? extends ExpressionInfo> getTrueExpression() {
        return this.trueExpression;
    }

    public UnresolvedExpressionInfo<? extends ExpressionInfo> getFalseExpression() {
        return this.falseExpression;
    }

    /**
     * �O�����Z�̏�����(��ꍀ)�̂݉�������ۑ�����ϐ�
     */
    private final UnresolvedConditionInfo<? extends ConditionInfo> condition;

    /**
     * �O�����Z�̏�������true�̂Ƃ��ɕԂ���鎮(���)�̖���������ۑ�����ϐ�
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> trueExpression;

    /**
     * �O�����Z�̏�������false�̂Ƃ��ɕԂ���鎮(��O��)�̖���������ۑ�����ϐ�
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> falseExpression;

}
