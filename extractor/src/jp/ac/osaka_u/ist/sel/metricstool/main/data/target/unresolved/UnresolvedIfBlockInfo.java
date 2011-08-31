package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������if�u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
public final class UnresolvedIfBlockInfo extends UnresolvedConditionalBlockInfo<IfBlockInfo> {

    /**
     * �O���̃u���b�N����^���āCif �u���b�N����������
     * 
     * @param outerSpace �O���̃u���b�N
     */
    public UnresolvedIfBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);

        this.sequentElseBlock = null;
    }

    /**
     * ���̖����� if �u���b�N����������
     * 
     * @param usingClass �����N���X
     * @param usingMethod �������\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     */
    @Override
    public IfBlockInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // ���� if���̈ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new IfBlockInfo(fromLine, fromColumn, toLine, toColumn);

        final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace = this.getOuterSpace();
        final LocalSpaceInfo outerSpace = unresolvedLocalSpace.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOuterUnit(outerSpace);

        // ����else�u���b�N������ꍇ�͉�������
        if (this.hasElseBlock()) {
            final UnresolvedElseBlockInfo unresolvedElseBlockInfo = this.getSequentElseBlock();
            final ElseBlockInfo sequentBlockInfo = unresolvedElseBlockInfo.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.setSequentElseBlock(sequentBlockInfo);
        }

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
    public void resolveInnerBlock(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        super.resolveInnerBlock(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);

        // ����else�u���b�N������ꍇ�͉�������
        if (this.hasElseBlock()) {
            final UnresolvedElseBlockInfo unresolvedElseBlockInfo = this.getSequentElseBlock();
            unresolvedElseBlockInfo.resolveInnerBlock(usingClass, usingMethod, classInfoManager,
                    fieldInfoManager, methodInfoManager);
        }
    }

    /**
     * �Ή�����else�u���b�N��Ԃ�
     * @return �Ή�����else�u���b�N�D�Ή�����else�u���b�N�����݂��Ȃ��ꍇ��null
     */
    public UnresolvedElseBlockInfo getSequentElseBlock() {
        return this.sequentElseBlock;
    }

    /**
     * �Ή�����else�u���b�N���Z�b�g����
     * @param elseBlock �Ή�����else�u���b�N
     */
    public void setSequentElseBlock(UnresolvedElseBlockInfo elseBlock) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == elseBlock) {
            throw new IllegalArgumentException("elseBlock is null");
        }

        this.sequentElseBlock = elseBlock;
    }

    /**
     * �Ή�����else�u���b�N�����݂��邩�ǂ����\��
     * @return �Ή�����else�u���b�N�����݂���Ȃ�true
     */
    public boolean hasElseBlock() {
        return null != this.sequentElseBlock;
    }

    /**
     * �Ή�����else�u���b�N��ۑ�����ϐ�
     */
    private UnresolvedElseBlockInfo sequentElseBlock;

}
