package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �N���X�̃R���X�g���N�^�Ăяo����\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public class ClassConstructorCallInfo extends ConstructorCallInfo<ClassTypeInfo> {

    /**
     * �^��^���ăR���X�g���N�^�Ăяo����������
     * 
     * @param classType �Ăяo���̌^
     * @param callee �Ăяo����Ă���R���X�g���N�^
     * @param ownerMethod �I�[�i�[���\�b�h 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ClassConstructorCallInfo(final ClassTypeInfo classType, final ConstructorInfo callee,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(classType, callee, ownerMethod, fromLine, fromColumn, toLine, toColumn);

    }

    /**
     * ���̃R���X�g���N�^�Ăяo���̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ���̃R���X�g���N�^�Ăяo���̃e�L�X�g�\���i�^�j��Ԃ�
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("new ");

        final TypeInfo type = this.getType();
        sb.append(type.getTypeName());

        sb.append("(");

        for (final ExpressionInfo argument : this.getArguments()) {
            sb.append(argument.getText());
            sb.append(",");
        }
        if (0 < this.getArguments().size()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append(")");

        return sb.toString();
    }

    @Override
    public ExecutableElementInfo copy() {

        final ClassTypeInfo classType = this.getType();
        final ConstructorInfo callee = this.getCallee();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ClassConstructorCallInfo newCall = new ClassConstructorCallInfo(classType, callee,
                ownerMethod, fromLine, fromColumn, toLine, toColumn);

        for (final ExpressionInfo argument : this.getArguments()) {
            newCall.addArgument((ExpressionInfo) argument.copy());
        }

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newCall.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newCall.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newCall;
    }
}
