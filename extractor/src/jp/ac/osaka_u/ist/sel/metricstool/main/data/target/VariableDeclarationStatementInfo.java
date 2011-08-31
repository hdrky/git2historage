package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �ϐ��錾���̏���ۗL����N���X
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public class VariableDeclarationStatementInfo extends SingleStatementInfo implements ConditionInfo {

    /**
     * �錾����Ă���ϐ��C���������C�ʒu����^���ď�����
     * �錾����Ă���ϐ�������������Ă���ꍇ�C���̃R���X�g���N�^���g�p����
     * 
     * @param variableDeclaration �錾����Ă��郍�[�J���ϐ�
     * @param initializationExpression ��������
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public VariableDeclarationStatementInfo(final LocalSpaceInfo ownerSpace,
            final LocalVariableUsageInfo variableDeclaration,
            final ExpressionInfo initializationExpression, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == variableDeclaration) {
            throw new IllegalArgumentException("declaredVariable is null");
        }

        this.variableDeclaration = variableDeclaration;
        this.variableDeclaration.setOwnerExecutableElement(this);
        this.variableDeclaration.getUsedVariable().setDeclarationStatement(this);

        if (null != initializationExpression) {
            this.initializationExpression = initializationExpression;
        } else {

            // ownerSpaceInfo�����\�b�h�܂��̓R���X�g���N�^�̎�
            if (ownerSpace instanceof CallableUnitInfo) {
                this.initializationExpression = new EmptyExpressionInfo(
                        (CallableUnitInfo) ownerSpace, toLine, toColumn - 1, toLine, toColumn - 1);
            }

            // ownerSpaceInfo���u���b�N���̎�
            else if (ownerSpace instanceof BlockInfo) {
                final CallableUnitInfo ownerMethod = ((BlockInfo) ownerSpace).getOwnerMethod();
                this.initializationExpression = new EmptyExpressionInfo(ownerMethod, toLine,
                        toColumn - 1, toLine, toColumn - 1);
            }

            // ����ȊO�̎��̓G���[
            else {
                throw new IllegalStateException();
            }
        }

        this.initializationExpression.setOwnerExecutableElement(this);
        this.ownerConditionalBlock = null;
    }

    /**
     * ���̐錾���Ő錾����Ă���ϐ���Ԃ�
     * 
     * @return ���̐錾���Ő錾����Ă���ϐ�
     */
    public final LocalVariableInfo getDeclaredLocalVariable() {
        return this.variableDeclaration.getUsedVariable();
    }

    /**
     * �錾���̕ϐ��g�p��Ԃ�
     * @return �錾���̕ϐ��g�p
     */
    public final LocalVariableUsageInfo getDeclaration() {
        return this.variableDeclaration;
    }

    /**
     * �錾����Ă���ϐ��̏���������Ԃ�
     * 
     * @return �錾����Ă���ϐ��̏��������D����������Ă��ꍇ��null
     */
    public final ExpressionInfo getInitializationExpression() {
        return this.initializationExpression;
    }

    /**
     * �錾����Ă���ϐ�������������Ă��邩�ǂ�����Ԃ�
     * 
     * @return �錾����Ă���ϐ�������������Ă����true
     */
    public boolean isInitialized() {
        return !(this.initializationExpression instanceof EmptyExpressionInfo);
    }

    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> usages = new TreeSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();

        usages.add(this.variableDeclaration);
        if (this.isInitialized()) {
            usages.addAll(this.getInitializationExpression().getVariableUsages());
        }

        return Collections.unmodifiableSet(usages);
    }

    /**
     * ��`���ꂽ�ϐ���Set��Ԃ�
     * 
     * @return ��`���ꂽ�ϐ���Set
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        definedVariables.add(this.getDeclaredLocalVariable());
        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo����Set
     */
    @Override
    public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        return this.isInitialized() ? this.getInitializationExpression().getCalls()
                : CallInfo.EmptySet;
    }

    /**
     * ���̕ϐ��錾���̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ���̕ϐ��錾���̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final LocalVariableInfo variable = this.getDeclaredLocalVariable();
        final TypeInfo type = variable.getType();
        sb.append(type.getTypeName());

        sb.append(" ");

        sb.append(variable.getName());

        if (this.isInitialized()) {

            sb.append(" = ");
            final ExpressionInfo expression = this.getInitializationExpression();
            sb.append(expression.getText());
        }

        sb.append(";");

        return sb.toString();
    }

    @Override
    public String toString() {
        return this.getText() + "// (" + this.getFromLine() + ": " + this.getFromColumn() + ", "
                + this.getToLine() + ": " + this.getToColumn() + ")";
    }

    /**
     * �錾����Ă���ϐ��̌^��Ԃ�
     * @return �錾����Ă���ϐ��̌^
     */
    public TypeInfo getType() {
        return this.variableDeclaration.getType();
    }

    /**
     * ���̎��œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        final Set<ReferenceTypeInfo> thrownExpressions = new HashSet<ReferenceTypeInfo>();
        if (this.isInitialized()) {
            thrownExpressions.addAll(this.getInitializationExpression().getThrownExceptions());
        }
        return Collections.unmodifiableSet(thrownExpressions);
    }

    /**
     * ���̎��������Ƃ��Ď���ConditionalBlockInfo�Ԃ�
     */
    @Override
    public final ConditionalBlockInfo getOwnerConditionalBlock() {
        return this.ownerConditionalBlock;
    }

    /**
     * ���̎��������Ƃ��Ď���ConditionalBlockInfo��ݒ肷��
     * ������null�ł��邱�Ƃ����e����.
     */
    @Override
    public void setOwnerConditionalBlock(final ConditionalBlockInfo ownerConditionalBlock) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        assert null == this.ownerConditionalBlock : "this.ownerConditionalBlock must be null!";
        this.ownerConditionalBlock = ownerConditionalBlock;
    }

    @Override
    public ExecutableElementInfo copy() {
        final LocalSpaceInfo outerUnit = this.getOwnerSpace();
        final LocalVariableUsageInfo variableDeclaration = (LocalVariableUsageInfo) this
                .getDeclaration().copy();
        final ExpressionInfo initializerExpression = null != this.getInitializationExpression() ? (ExpressionInfo) this
                .getInitializationExpression().copy()
                : null;
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final VariableDeclarationStatementInfo newVariableDeclaration = new VariableDeclarationStatementInfo(
                outerUnit, variableDeclaration, initializerExpression, fromLine, fromColumn,
                toLine, toColumn);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        newVariableDeclaration.setOwnerConditionalBlock(ownerConditionalBlock);

        return newVariableDeclaration;
    }

    /**
     * �錾����Ă���ϐ���\���t�B�[���h
     */
    private final LocalVariableUsageInfo variableDeclaration;

    /**
     * �錾����Ă���ϐ��̏���������\���t�B�[���h
     */
    private final ExpressionInfo initializationExpression;

    /**
     * ���̕ϐ��錾���������Ƃ��Ď���ConditionalBlockInfo��ۑ����邽�߂̃t�B�[���h
     */
    private ConditionalBlockInfo ownerConditionalBlock;
}
