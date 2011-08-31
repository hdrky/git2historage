package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������z��ɑ΂���v�f�̎Q�Ƃ�\�����߂̃N���X�D�ȉ��̏������D
 * 
 * @author kou-tngt, higo
 * @see UnresolvedExpressionInfo
 */
public final class UnresolvedArrayElementUsageInfo extends
        UnresolvedExpressionInfo<ArrayElementUsageInfo> {

    /**
     * �v�f���Q�Ƃ��ꂽ�z��̌^��^����.
     * 
     * @param qualifierArrayType �v�f���Q�Ƃ��ꂽ�z��̌^
     * @param indexExpression �Q�Ƃ��ꂽ�v�f�̃C���f�b�N�X
     */
    public UnresolvedArrayElementUsageInfo(final UnresolvedExpressionInfo<?> qualifierArrayType,
            final UnresolvedExpressionInfo<?> indexExpression) {

        if (null == qualifierArrayType) {
            throw new NullPointerException("ownerArrayType is null.");
        }
        this.qualifierArrayType = qualifierArrayType;
        this.indexExpression = indexExpression;
        this.resolvedInfo = null;
    }

    /**
     * �������z��v�f�̎Q�Ƃ��������C�����ςݎQ�Ƃ�Ԃ��D
     * 
     * @param usingClass �������z��v�f�Q�Ƃ��s���Ă���N���X
     * @param usingMethod �������z��v�f�Q�Ƃ��s���Ă��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @return �����ςݎQ��
     */
    @Override
    public ArrayElementUsageInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == fieldInfoManager) || (null == methodInfoManager)) {
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

        // �v�f�g�p�̃C���f�b�N�X�𖼑O����
        final UnresolvedExpressionInfo<?> unresolvedIndexExpression = this.getIndexExpression();
        final ExpressionInfo indexExpression = unresolvedIndexExpression.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        assert indexExpression != null : "method \"resolve\" returned null!";

        // �v�f�g�p���������Ă���("."�̑O�̂���)����`�^���擾
        final UnresolvedExpressionInfo<?> unresolvedQualifierUsage = this.getQualifierArrayType();
        ExpressionInfo qualifierUsage = unresolvedQualifierUsage.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert qualifierUsage != null : "method \"resolve\" returned null!";

        // �e������ł��Ȃ��ꍇ���z��̗v�f�g�p���쐬���ĕԂ�
        // ����������ƁCUnknownEntityUsageInfo��Ԃ������K�؂�������Ȃ�
        this.resolvedInfo = new ArrayElementUsageInfo(indexExpression, qualifierUsage, usingMethod,
                fromLine, fromColumn, toLine, toColumn);

        return this.resolvedInfo;
    }

    /**
     * �v�f���Q�Ƃ��ꂽ�z��̌^��Ԃ�
     * 
     * @return �v�f���Q�Ƃ��ꂽ�z��̌^
     */
    public UnresolvedExpressionInfo<?> getQualifierArrayType() {
        return this.qualifierArrayType;
    }

    /**
     * �Q�Ƃ��ꂽ�v�f�̃C���f�b�N�X��Ԃ�
     * 
     * @return�@�Q�Ƃ��ꂽ�v�f�̃C���f�b�N�X
     */
    public UnresolvedExpressionInfo<?> getIndexExpression() {
        return this.indexExpression;
    }

    /**
     * �v�f���Q�Ƃ��ꂽ�z��̌^
     */
    private final UnresolvedExpressionInfo<?> qualifierArrayType;

    /**
     * �z��v�f�g�p�̃C���f�b�N�X���i�[����ϐ�
     */
    private final UnresolvedExpressionInfo<?> indexExpression;

}
