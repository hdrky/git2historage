package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * switch ���� case �G���g����\���N���X
 * 
 * @author higo
 */
public class UnresolvedCaseEntryInfo extends UnresolvedUnitInfo<CaseEntryInfo> implements
        UnresolvedStatementInfo<CaseEntryInfo> {

    /**
     * �Ή����� switch �u���b�N���ƃ��x������^���� case �G���g����������
     * 
     * @param ownerSwitchBlock �Ή����� switch �u���b�N
     * @param label ���x��
     * 
     */
    public UnresolvedCaseEntryInfo(final UnresolvedSwitchBlockInfo ownerSwitchBlock,
            final UnresolvedCaseLabelInfo label) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerSwitchBlock) || (null == label)) {
            throw new IllegalArgumentException();
        }
        this.ownerSwitchBlock = ownerSwitchBlock;
        this.label = label;
    }

    /**
     * �Ή����� switch �u���b�N����^���� case �G���g�����������D
     * �Ȃ��C���̃R���X�g���N�^�� default �G���g���p�̂��̂ł���D
     * 
     * @param ownerSwitchBlock �Ή����� switch �u���b�N
     */
    protected UnresolvedCaseEntryInfo(final UnresolvedSwitchBlockInfo ownerSwitchBlock,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerSwitchBlock) {
            throw new IllegalArgumentException("ownerSwitchBlock is null");
        }
        this.ownerSwitchBlock = ownerSwitchBlock;
        this.outerUnit = outerUnit;
        this.label = null;
    }

    /**
     * ���̖����� case �G���g������������
     * 
     * @param usingClass �����N���X
     * @param usingMethod �������\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     */
    @Override
    public CaseEntryInfo resolve(final TargetClassInfo usingClass,
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

        // ���� case �G���g���������� switch �����擾
        final UnresolvedSwitchBlockInfo unresolvedOwnerSwitchBlock = this.getOwnerSwitchBlock();
        final SwitchBlockInfo ownerSwitchBlock = unresolvedOwnerSwitchBlock.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // ���� case �G���g���̃��x�����擾
        final UnresolvedCaseLabelInfo unresolvedLabel = this.getLabel();
        final ExpressionInfo label = unresolvedLabel.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // ���� case �G���g���̈ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        //�@�����ς� case �G���g���I�u�W�F�N�g���쐬
        this.resolvedInfo = new CaseEntryInfo(ownerSwitchBlock, label, fromLine, fromColumn,
                toLine, toColumn);
        return this.resolvedInfo;
    }

    /**
     * ���� case �G���g���������� switch �u���b�N��Ԃ�
     * 
     * @return ���� case �G���g���������� switch �u���b�N
     */
    public final UnresolvedSwitchBlockInfo getOwnerSwitchBlock() {
        return this.ownerSwitchBlock;
    }

    /**
     * ���� case �G���g���̃��x����Ԃ�
     * 
     * @return ���� case �G���g���̃��x��
     */
    public final UnresolvedCaseLabelInfo getLabel() {
        return this.label;
    }

    @Override
    public UnresolvedCallableUnitInfo<? extends CallableUnitInfo> getOuterCallableUnit() {
        final UnresolvedLocalSpaceInfo<?> outerUnit = (UnresolvedLocalSpaceInfo<?>) this
                .getOuterUnit();
        return outerUnit instanceof UnresolvedCallableUnitInfo<?> ? (UnresolvedCallableUnitInfo<? extends CallableUnitInfo>) outerUnit
                : outerUnit.getOuterCallableUnit();
    }

    @Override
    public UnresolvedClassInfo getOuterClass() {
        return this.getOuterCallableUnit().getOuterClass();
    }

    @Override
    public UnresolvedUnitInfo<? extends UnitInfo> getOuterUnit() {
        return this.outerUnit;
    }

    @Override
    public void setOuterUnit(UnresolvedUnitInfo<? extends UnitInfo> outerUnit) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == outerUnit) {
            throw new IllegalArgumentException();
        }
        this.outerUnit = outerUnit;
    }

    /**
     * ���� case �G���g���������� switch �u���b�N��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedSwitchBlockInfo ownerSwitchBlock;

    /**
     * ���� case �G���g���̃��x����ۑ�����ϐ�
     */
    private final UnresolvedCaseLabelInfo label;

    private UnresolvedUnitInfo<? extends UnitInfo> outerUnit;
}
