package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;


/**
 * �������񍀉��Z���i�[���邽�߂̃N���X
 * 
 * @author higo
 * 
 */
public class UnresolvedBinominalOperationInfo extends
        UnresolvedExpressionInfo<BinominalOperationInfo> {

    /**
     * ���Z�q��2�̃I�y�����h��^���ď���������
     * 
     * @param operator ���Z�q
     * @param firstOperand ���i�������j�I�y�����h
     * @param secondOperand ���i�������j�I�y�����h
     */
    public UnresolvedBinominalOperationInfo(final OPERATOR operator,
            final UnresolvedExpressionInfo<?> firstOperand,
            final UnresolvedExpressionInfo<?> secondOperand) {

        if ((null == operator) || (null == firstOperand) || (null == secondOperand)) {
            throw new NullPointerException();
        }

        this.operator = operator;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
        this.resolvedInfo = null;
    }

    /**
     * �������񍀉��Z���������C���̌^��Ԃ��D
     * 
     * @param usingClass �������񍀉��Z���s���Ă���N���X
     * @param usingMethod �������񍀉��Z���s���Ă��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @return �����ςݓ񍀉��Z�i�܂�C���Z���ʂ̌^�j
     */
    @Override
    public BinominalOperationInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final OPERATOR operator = this.getOperator();
        final UnresolvedExpressionInfo<?> unresolvedFirstOperand = this.getFirstOperand();
        final UnresolvedExpressionInfo<?> unresolvedSecondOperand = this.getSecondOperand();
        final ExpressionInfo firstOperand = unresolvedFirstOperand.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final ExpressionInfo secondOperand = unresolvedSecondOperand.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        //�@�ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        /*// �v�f�g�p�̃I�[�i�[�v�f��Ԃ�
        final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                .getOwnerExecutableElement();
        final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);*/

        this.resolvedInfo = new BinominalOperationInfo(operator, firstOperand, secondOperand,
                usingMethod, fromLine, fromColumn, toLine, toColumn);
        /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/

        return this.resolvedInfo;
    }

    /**
     * ���Z�q���擾����
     * 
     * @return ���Z�q
     */
    public OPERATOR getOperator() {
        return this.operator;
    }

    /**
     * ���i�������j�I�y�����h���擾����
     * 
     * @return ���i�������j�I�y�����h
     */
    public UnresolvedExpressionInfo<?> getFirstOperand() {
        return this.firstOperand;
    }

    /**
     * ���i�������j�I�y�����h���擾����
     * 
     * @return ���i�������j�I�y�����h
     */
    public UnresolvedExpressionInfo<?> getSecondOperand() {
        return this.secondOperand;
    }

    /**
     * ���Z�q���Z�b�g����
     * 
     * @param operator ���Z�q
     */
    public void setOperator(final OPERATOR operator) {

        if (null == operator) {
            throw new NullPointerException();
        }

        this.operator = operator;
    }

    /**
     * ���i�������j�I�y�����h���Z�b�g����
     * 
     * @param firstOperand ���i�������j�I�y�����h
     */
    public void setFirstOperand(final UnresolvedExpressionInfo<?> firstOperand) {

        if (null == firstOperand) {
            throw new NullPointerException();
        }

        this.firstOperand = firstOperand;
    }

    /**
     * ���i�������j�I�y�����h���Z�b�g����
     * 
     * @param secondOperand ���i�������j�I�y�����h
     */
    public void setSecondOperand(final UnresolvedExpressionInfo<?> secondOperand) {

        if (null == secondOperand) {
            throw new NullPointerException();
        }

        this.secondOperand = secondOperand;
    }

    private OPERATOR operator;

    private UnresolvedExpressionInfo<?> firstOperand;

    private UnresolvedExpressionInfo<?> secondOperand;

}
