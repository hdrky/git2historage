package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;


/**
 * �������R���X�g���N�^�Ăяo����ۑ����邽�߂̃N���X
 * 
 * @author t-miyake, higo
 *
 */
public abstract class UnresolvedConstructorCallInfo<T extends UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>, R extends ConstructorCallInfo<? extends ReferenceTypeInfo>>
        extends UnresolvedCallInfo<R> {

    /**
     * �R���X�g���N�^�Ăяo�������s�����Q�ƌ^��^���ăI�u�W�F�N�g��������
     * 
     * @param unresolvedReferenceType �R���X�g���N�^�Ăяo�������s�����^
     */
    public UnresolvedConstructorCallInfo(final T unresolvedReferenceType) {

        if (null == unresolvedReferenceType) {
            throw new IllegalArgumentException();
        }

        this.unresolvedReferenceType = unresolvedReferenceType;
    }

    /**
     * �R���X�g���N�^�Ăяo�������s�����Q�ƌ^��^���ď�����
     * @param unresolvedReferenceType �R���X�g���N�^�Ăяo�������s�����^
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public UnresolvedConstructorCallInfo(final T unresolvedReferenceType,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        this(unresolvedReferenceType);

        this.setOuterUnit(outerUnit);
        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    /**
     * ���̖������R���X�g���N�^�Ăяo���̌^��Ԃ�
     * 
     * @return ���̖������R���X�g���N�^�Ăяo���̌^
     */
    public T getReferenceType() {
        return this.unresolvedReferenceType;
    }

    private final T unresolvedReferenceType;

}
