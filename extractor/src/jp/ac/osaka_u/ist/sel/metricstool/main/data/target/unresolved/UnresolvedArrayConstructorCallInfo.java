package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������z��R���X�g���N�^�Ăяo����\���N���X
 * 
 * @author higo
 *
 */
public class UnresolvedArrayConstructorCallInfo extends
        UnresolvedConstructorCallInfo<UnresolvedArrayTypeInfo, ArrayConstructorCallInfo> {

    /**
     * �z��R���X�g���N�^�Ăяo�������s�����Q�ƌ^��^���ăI�u�W�F�N�g��������
     * 
     * @param unresolvedArrayType �R���X�g���N�^�Ăяo�������s�����^
     */
    public UnresolvedArrayConstructorCallInfo(final UnresolvedArrayTypeInfo unresolvedArrayType) {

        super(unresolvedArrayType);

        this.indexExpressions = new TreeMap<Integer, UnresolvedExpressionInfo<? extends ExpressionInfo>>();
    }

    /**
     * �z��R���X�g���N�^�Ăяo�������s�����^�ƈʒu����^���ď�����
     * @param unresolvedArrayType �R���X�g���N�^�Ăяo�������s�����^
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public UnresolvedArrayConstructorCallInfo(final UnresolvedArrayTypeInfo unresolvedArrayType,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        this(unresolvedArrayType);
        this.setOuterUnit(outerUnit);
        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    /**
     * ���O�������s��
     */
    @Override
    public ArrayConstructorCallInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

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

        //�@�ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // �R���X�g���N�^�̃V�O�l�`�����擾
        final List<ExpressionInfo> actualParameters = super.resolveArguments(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        final List<ReferenceTypeInfo> typeArguments = super.resolveTypeArguments(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        //�@�R���X�g���N�^�̌^������
        final UnresolvedArrayTypeInfo unresolvedArrayType = this.getReferenceType();
        final ArrayTypeInfo arrayType = unresolvedArrayType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new ArrayConstructorCallInfo(arrayType, usingMethod, fromLine,
                fromColumn, toLine, toColumn);
        this.resolvedInfo.addArguments(actualParameters);
        this.resolvedInfo.addTypeArguments(typeArguments);

        // �C���f�b�N�X�̎�������
        for (int dimension = 1; dimension <= arrayType.getDimension(); dimension++) {

            final UnresolvedExpressionInfo<? extends ExpressionInfo> unresolvedIndexExpression = this
                    .getIndexExpression(dimension);
            final ExpressionInfo indexExpression;
            if (null != unresolvedIndexExpression) {
                indexExpression = unresolvedIndexExpression.resolve(usingClass, usingMethod,
                        classInfoManager, fieldInfoManager, methodInfoManager);
            } else {
                indexExpression = new EmptyExpressionInfo(usingMethod, fromLine, fromColumn,
                        toLine, toColumn);
            }
            this.resolvedInfo.addIndexExpression(dimension, indexExpression);
        }

        return this.resolvedInfo;
    }

    /**
     * �C���f�b�N�X�̎����Z�b�g
     * 
     * @param dimension �z��̎�����\��
     * @param indexExpression �z��̗v�f�w�蕔���̎���\��
     */
    public void addIndexExpression(final int dimension,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> indexExpression) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == indexExpression) {
            throw new IllegalArgumentException("indexExpression is null");
        }

        this.indexExpressions.put(dimension, indexExpression);
    }

    /**
     * �C���f�b�N�X�̎����擾
     * 
     * @return �C���f�b�N�X�̎�
     */
    public List<UnresolvedExpressionInfo<? extends ExpressionInfo>> getIndexExpressions() {

        final List<UnresolvedExpressionInfo<? extends ExpressionInfo>> indexExpressions = new ArrayList<UnresolvedExpressionInfo<? extends ExpressionInfo>>();
        for (final UnresolvedExpressionInfo<? extends ExpressionInfo> indexExpression : this.indexExpressions
                .values()) {
            indexExpressions.add(indexExpression);
        }
        return Collections.unmodifiableList(indexExpressions);
    }

    public UnresolvedExpressionInfo<? extends ExpressionInfo> getIndexExpression(final int dimension) {
        return this.indexExpressions.get(dimension);
    }

    /**
     * �C���f�b�N�X�̎���ۑ����邽�߂̕ϐ�
     */
    private SortedMap<Integer, UnresolvedExpressionInfo<? extends ExpressionInfo>> indexExpressions;
}
