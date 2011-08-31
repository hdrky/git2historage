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
 * ������ else �u���b�N��\���N���X
 * 
 * @author higo
 */
public final class UnresolvedElseBlockInfo extends UnresolvedBlockInfo<ElseBlockInfo> implements
        UnresolvedSubsequentialBlockInfo<UnresolvedIfBlockInfo> {

    /**
     * �O���̃u���b�N�ƑΉ����� if �u���b�N��^���āCelse �u���b�N����������
     * 
     * @param ownerIfBlock
     * @param outerSpace �O���̃u���b�N
     */
    public UnresolvedElseBlockInfo(final UnresolvedIfBlockInfo ownerIfBlock,
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);

        if (null == ownerIfBlock) {
            throw new IllegalArgumentException("ownerIfBlock is null");
        }

        this.ownerIfBlock = ownerIfBlock;
    }

    /**
     * ���̖����� else �u���b�N����������
     * 
     * @param usingClass �����N���X
     * @param usingMethod �������\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     */
    @Override
    public ElseBlockInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // ���� else �u���b�N�̈ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new ElseBlockInfo(fromLine, fromColumn, toLine, toColumn);

        // ���� else �u���b�N�������� if �u���b�N���擾
        final UnresolvedIfBlockInfo unresolvedOwnerIfBlock = this.getOwnerBlock();
        final IfBlockInfo ownerIfBlock = unresolvedOwnerIfBlock.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOwnerBlock(ownerIfBlock);

        final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace = this.getOuterSpace();
        final LocalSpaceInfo outerSpace = unresolvedLocalSpace.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOuterUnit(outerSpace);

        return this.resolvedInfo;
    }

    /**
     * ���� else �u���b�N�ƑΉ����� if �u���b�N��Ԃ�
     * ���̃��\�b�h�͏����p�~�\��ł���C�g�p�͐�������Ȃ�
     * {@link UnresolvedElseBlockInfo#getOwnerBlock()}���g�p���ׂ��ł���D
     * 
     * @return ���� else �u���b�N�ƑΉ����� if �u���b�N
     * @deprecated
     */
    public UnresolvedIfBlockInfo getOwnerIfBlock() {
        return this.ownerIfBlock;
    }

    /**
     * ���� else �u���b�N�ƑΉ����� if �u���b�N��Ԃ�
     * 
     * @return ���� else �u���b�N�ƑΉ����� if �u���b�N
     */
    @Override
    public UnresolvedIfBlockInfo getOwnerBlock() {
        return this.ownerIfBlock;
    }

    /**
     * ���� else �u���b�N�ƑΉ����� if �u���b�N��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedIfBlockInfo ownerIfBlock;

}
