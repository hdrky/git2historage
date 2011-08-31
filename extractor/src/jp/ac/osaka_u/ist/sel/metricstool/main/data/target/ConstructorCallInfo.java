package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * �R���X�g���N�^�Ăяo����ۑ�����ϐ�
 * 
 * @author higo

 * @param <T> �^�̏��C�N���X�^���z��^��
 */
@SuppressWarnings("serial")
public abstract class ConstructorCallInfo<T extends ReferenceTypeInfo> extends
        CallInfo<ConstructorInfo> {

    /**
     * �^��^���ăR���X�g���N�^�Ăяo����������
     * 
     * @param referenceType �Ăяo���̌^
     * @param callee �Ăяo����Ă���R���X�g���N�^
     * @param ownerMethod �I�[�i�[���\�b�h 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I���� 
     */
    public ConstructorCallInfo(final T referenceType, final ConstructorInfo callee,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(callee, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == referenceType) {
            throw new NullPointerException();
        }

        this.referenceType = referenceType;
    }

    /**
     * �R���X�g���N�^�Ăяo���̌^��Ԃ�
     */
    @Override
    public T getType() {
        return this.referenceType;
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo����Set
     */
    @Override
    public final Set<CallInfo<?>> getCalls() {
        final Set<CallInfo<?>> calls = new HashSet<CallInfo<?>>();
        calls.add(this);
        for (final ExpressionInfo argument : this.getArguments()) {
            calls.addAll(argument.getCalls());
        }
        return Collections.unmodifiableSet(calls);
    }

    private final T referenceType;

}
