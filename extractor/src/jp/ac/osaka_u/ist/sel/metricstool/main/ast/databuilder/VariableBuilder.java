package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ModifiersDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.NameStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeDescriptionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.VariableDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.VariableDefinitionStateManager.VARIABLE_STATE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;


/**
 * �ϐ��f�[�^���\�z���钊�ۃN���X.
 * 
 * ���̃N���X�̃T�u�N���X�́C�R���X�g���N�^�ɕϐ��錾���̏�ԊǗ������� {@link VariableDefinitionStateManager} ��^���C
 * {@link #buildVariable(String[], UnresolvedTypeInfo, ModifierInfo[])}���\�b�h���������邱�ƂŁC
 * �C�ӂ̕ϐ��f�[�^�ɑΉ������邱�Ƃ��ł���D
 * 
 * @author kou-tngt
 *
 * @param <TVar> �\�z����ϐ��̌^
 * @param <TUnit> �\�z����ϐ�����ۑ����郆�j�b�g
 */
public abstract class VariableBuilder<TVar extends UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>>, TUnit extends UnresolvedUnitInfo<? extends UnitInfo>>
        extends CompoundDataBuilder<TVar> {

    /**
     * �����ŗ^����ꂽ�\�z�f�[�^�Ǘ��ҁC�ϐ��錾�Ɋւ����ԊǗ��҂ƁC�f�t�H���g�̏C���q���r���_�[�C�^���r���_�[�C���O���r���_�[��p���ď���������D
     * @param buildDataManager �\�z�f�[�^�Ǘ���
     * @param stateManager �ϐ��錾�Ɋւ����ԊǗ���
     */
    public VariableBuilder(final BuildDataManager buildDataManager,
            final VariableDefinitionStateManager stateManager) {
        this(buildDataManager, stateManager, new ModifiersBuilder(), new TypeBuilder(
                buildDataManager), new NameBuilder());
    }

    /**
     * �����ŗ^����ꂽ�ϐ��錾�Ɋւ����ԊǗ��ҁC�C���q���r���_�[�C�^���r���_�[�C���O���r���_�[��p���ď���������D
     * @param variableStateManager �ϐ��錾�Ɋւ����ԊǗ���
     * @param modifiersBuilder�@�C���q���r���_�[
     * @param typeBuilder�@�^���r���_�[
     * @param nameBuilder�@���O���r���_�[
     */
    public VariableBuilder(final BuildDataManager buildDataManager,
            final VariableDefinitionStateManager variableStateManager,
            final ModifiersBuilder modifiersBuilder, final TypeBuilder typeBuilder,
            final NameBuilder nameBuilder) {

        if (null == buildDataManager) {
            throw new IllegalArgumentException("buildDataManager is null");
        }

        if (null == variableStateManager) {
            throw new IllegalArgumentException("stateManager is null.");
        }

        if (null == typeBuilder) {
            throw new IllegalArgumentException("typeBuilder is null.");
        }

        if (null == nameBuilder) {
            throw new IllegalArgumentException("nameBuilder is null.");
        }

        //null�`�F�b�N�I��

        this.buildDataManager = buildDataManager;

        //��Ԓʒm���󂯎�肽�����̂�o�^
        this.variableStateManager = variableStateManager;
        this.addStateManager(variableStateManager);
        this.addStateManager(this.typeStateManager);
        this.addStateManager(new ModifiersDefinitionStateManager());
        this.addStateManager(new NameStateManager());

        //�����r���_�[��o�^
        this.addInnerBuilder(modifiersBuilder);
        this.addInnerBuilder(typeBuilder);
        this.addInnerBuilder(nameBuilder);

        this.modifiersBuilder = modifiersBuilder;
        this.typeBuilder = typeBuilder;
        this.nameBuilder = nameBuilder;
    }

    /**
     * ��ԕω��C�x���g�̒ʒm���󂯎�郁�\�b�h�D
     * @param event ��ԕω��C�x���g
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder#stateChanged(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent)
     */
    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType eventType = event.getType();
        if (eventType.equals(VARIABLE_STATE.EXIT_VARIABLE_DEF)) {

            //�ϐ��錾������o���̂ŁC�ϐ�����o�^����
            //�X�R�[�v�̊֌W��C�錾���I���Ȃ��Ɠo�^���Ă͂����Ȃ�
            final AstVisitEvent trigger = event.getTrigger();

            final TUnit currentUnit = this.validateDefinitionSpace(this.buildDataManager
                    .getCurrentUnit());
            if (null != currentUnit) {
                this.endVariableBuild(currentUnit, trigger.getStartLine(),
                        trigger.getStartColumn(), trigger.getEndLine(), trigger.getEndColumn());
            }

        } else if (this.variableStateManager.isInDefinition()) {
            if (eventType
                    .equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.ENTER_MODIFIERS_DEF)) {
                //�C���q���̍\�z�J�n
                if (null != this.modifiersBuilder) {
                    this.modifiersBuilder.activate();
                }
            } else if (eventType
                    .equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.EXIT_MODIFIERS_DEF)) {
                //�C���q���̍\�z�I��
                //�����ɍ\�z�����f�[�^���X�^�b�N�ցi�ϐ��錾�͏��������ȍ~�œ���q�ɂȂ邱�Ƃ����邽�߁C�\�z�����f�[�^�͂������Ǝ擾���Ă����j
                if (null != this.modifiersBuilder) {
                    this.modifiersBuilder.deactivate();
                    this.builtModifiersStack.push(this.modifiersBuilder.popLastBuiltData());
                    modifiersBuilder.clearBuiltData();
                }
            } else if (eventType.equals(TypeDescriptionStateManager.TYPE_STATE.ENTER_TYPE)) {
                //�^���\�z�J�n
                this.typeBuilder.activate();
            } else if (eventType.equals(TypeDescriptionStateManager.TYPE_STATE.EXIT_TYPE)) {
                if (!this.typeStateManager.isEntered()) {
                    //�^���͂���P�̂ł������q�ɂȂ��Ă���̂ŁC
                    //�Ƃ肠������ԊO���̌^��`���I����Ă�����^���\�z�I��
                    //�\�z�����^���������Ɏ擾���ăX�^�b�N��
                    this.typeBuilder.deactivate();
                    this.pushBuiltType(this.typeBuilder.popLastBuiltData());
                    this.typeBuilder.clearBuiltData();
                }
            } else if (eventType.equals(NameStateManager.NAME_STATE.ENTER_NAME)) {
                //���O�\�z�J�n
                this.nameBuilder.activate();
            } else if (eventType.equals(NameStateManager.NAME_STATE.EXIT_NAME)) {
                //���O�\�z�I��
                //�������������Ǝ擾���Ă����ɃX�^�b�N��
                this.nameBuilder.deactivate();
                this.builtNameStack.push(this.nameBuilder.popLastBuiltData());
                this.nameBuilder.clearBuiltData();
            }
        }
    }

    protected void pushBuiltType(final UnresolvedTypeInfo<? extends TypeInfo> builtType) {
        this.builtTypeStack.push(builtType);
    }
    
    /**
     * �ϐ��f�[�^���\�z���钊�ۃ��\�b�h�D
     * �\�z�����ϐ��f�[�^���\�z�f�[�^�Ǘ��҂ɓn�������ꍇ�����̃��\�b�h�ōs���D
     * 
     * @param name�@�ϐ��̖��O
     * @param type�@�ϐ��̌^
     * @param modifiers�@�ϐ��̏C���q
     * @param definitionUnit �ϐ���錾���Ă��郆�j�b�g
     * @param startLine �ϐ���`���̊J�n�s
     * @param startColumn�@�ϐ���`���̊J�n��
     * @param endLine�@�ϐ���`���̏I���s
     * @param endColumn�@�ϐ���`���̏I����
     * @return�@�\�z�����ϐ����
     */
    protected abstract TVar buildVariable(final String[] name,
            final UnresolvedTypeInfo<? extends TypeInfo> type, final ModifierInfo[] modifiers,
            final TUnit definitionUnit, final int startLine, final int startColumn,
            final int endLine, final int endColumn);

    /**
     * �ϐ���`���I�������Ƃ��ɌĂяo����C {@link #buildVariable(String[], UnresolvedTypeInfo, ModifierInfo[])}
     * ���\�b�h���Ăяo���ĕϐ��f�[�^���\�z����D
     * 
     * ���̃��\�b�h���I�[�o�[���C�h���邱�Ƃŕϐ���`�̏I�����̓����ύX���邱�Ƃ��ł���D
     * 
     * @param �ϐ���錾���Ă��郆�j�b�g
     * @param startLine �ϐ���`���̊J�n�s
     * @param startColumn�@�ϐ���`���̊J�n��
     * @param endLine�@�ϐ���`���̏I���s
     * @param endColumn�@�ϐ���`���̏I����
     */
    protected void endVariableBuild(final TUnit definitionUnit, final int startLine,
            final int startColumn, final int endLine, final int endColumn) {
        final TVar variable = this.buildVariable(this.getName(), this.getType(), this
                .getModifiers(), definitionUnit, startLine, startColumn, endLine, endColumn);

        this.registBuiltData(variable);
    }

    /**
     * �����ŗ^�����ă��j�b�g���ϐ����`���郆�j�b�g�Ƃ��ėL���ł��邩�ǂ������f����
     * 
     * @param definitionUnit �ϐ����`���郆�j�b�g
     * @return �ϐ����`�����ԂƂ��ėL���ł���΂��̃��j�b�g�C�L���łȂ����null
     */
    protected abstract TUnit validateDefinitionSpace(
            final UnresolvedUnitInfo<? extends UnitInfo> definitionUnit);

    /**
     * �ł��Ō�ɍ\�z�����ϐ�����Ԃ��D
     * @return�@�ł��Ō�ɍ\�z�����ϐ���
     */
    private String[] getName() {
        return this.builtNameStack.pop();
    }

    /**
     * �ł��Ō�ɍ\�z�����ϐ��Ɋւ���C���q�̔z���Ԃ��D
     * @return�@�ł��Ō�ɍ\�z�����ϐ��Ɋւ���C���q�̔z��
     */
    private ModifierInfo[] getModifiers() {
        if (null != this.builtModifiersStack) {
            return this.builtModifiersStack.pop();
        } else {
            return EMPTY_MODIFIERS;
        }
    }

    /**
     * �ł��Ō�ɍ\�z�����ϐ��̌^��Ԃ��D
     * @return�@�ł��Ō�ɍ\�z�����ϐ��̌^
     */
    private UnresolvedTypeInfo<? extends TypeInfo> getType() {
        return this.builtTypeStack.pop();
    }

    protected final BuildDataManager buildDataManager;

    /**
     * �ϐ���`���Ɋւ����Ԃ��Ǘ�����X�e�[�g�}�l�[�W��
     */
    protected final VariableDefinitionStateManager variableStateManager;

    /**
     * �^�p�����[�^�L�q���Ɋւ����Ԃ��Ǘ�����X�e�[�g�}�l�[�W��
     */
    private final TypeDescriptionStateManager typeStateManager = new TypeDescriptionStateManager();

    /**
     * �C���q�����\�z����r���_�[
     */
    private final ModifiersBuilder modifiersBuilder;

    /**
     * �^�����\�z����r���_�[
     */
    private final TypeBuilder typeBuilder;

    /**
     * ���O�����\�z����r���_�[
     */
    private final NameBuilder nameBuilder;

    /**
     * �\�z�����^�����\�z�シ���Ɋi�[���Ă����X�^�b�N
     */
    private final Stack<UnresolvedTypeInfo<? extends TypeInfo>> builtTypeStack = new Stack<UnresolvedTypeInfo<? extends TypeInfo>>();

    /**
     * �\�z�����C���q�����\�z�シ���Ɋi�[���Ă����X�^�b�N
     */
    private final Stack<ModifierInfo[]> builtModifiersStack = new Stack<ModifierInfo[]>();

    /**
     * �\�z�������O�����\�z�シ���Ɋi�[���Ă����X�^�b�N
     */
    private final Stack<String[]> builtNameStack = new Stack<String[]>();

    /**
     * ��̏C���q�z���\���萔
     */
    private static final ModifierInfo[] EMPTY_MODIFIERS = new ModifierInfo[0];
}
