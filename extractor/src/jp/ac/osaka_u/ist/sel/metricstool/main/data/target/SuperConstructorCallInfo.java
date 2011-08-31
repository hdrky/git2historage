package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

/**
 * super��p�����R���X�g���N�^�Ăяo����\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public class SuperConstructorCallInfo extends ClassConstructorCallInfo {

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
    public SuperConstructorCallInfo(final ClassTypeInfo classType, final ConstructorInfo callee,
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

        sb.append("super(");

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
}
