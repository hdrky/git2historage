package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ClassDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ClassDefinitionStateManager.CLASS_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ModifiersDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.NameStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.*;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;


/**
 * �N���X���̍\�z���s���r���_�[�D
 * 
 * @author kou-tngt
 *
 */
public class ClassBuilder extends CompoundDataBuilder<UnresolvedClassInfo> {

    /**
     * �����ŗ^����ꂽ�\�z�f�[�^�Ǘ����s��BuildDataManager��
     * �f�t�H���g�̏C���q�r���_�[�C���O�r���_�[��p���ď���������D
     * 
     * @param targetDataManager�@�r���_�[�����p����\�z�f�[�^�Ǘ���
     */
    public ClassBuilder(final BuildDataManager targetDataManager) {
        this(targetDataManager, new ModifiersBuilder(), new NameBuilder());
    }

    /**
     * �����ŗ^����ꂽ�\�z�f�[�^�Ǘ����s��BuildDataManager
     * �C���q�r���_�[�C���O�r���_�[��p���ď���������D
     * 
     * @param targetDataManager�@�r���_�[�����p����\�z�f�[�^�Ǘ���
     */
    public ClassBuilder(final BuildDataManager targetDataManager,
            final ModifiersBuilder modifiersBuilder, final NameBuilder nameBuilder) {

        //�f�[�^�Ǘ��҂�null���ƍ���D
        if (null == targetDataManager) {
            throw new NullPointerException("builderManager is null.");
        }

        //���O�\�z���ł��Ȃ��ƍ���D
        if (null == nameBuilder) {
            throw new NullPointerException("nameBuilder is null.");
        }

        //��ԕω���ʒm���ė~�������̂�o�^����D
        this.addStateManager(this.classStateManager);
        this.addStateManager(new ModifiersDefinitionStateManager());
        this.addStateManager(new NameStateManager());

        this.buildManager = targetDataManager;
        this.modifiersBuilder = modifiersBuilder;
        this.nameBuilder = nameBuilder;

        //�����Ŏg���r���_�[��o�^���āC�����炪�A�N�e�B�u�ɂȂ������Ɏ����I�Ƀr�W�^�[����̃C�x���g���͂��悤�ɂ���D
        //�f�t�H���g�Ŕ�A�N�e�B�u��ԂɃZ�b�g�����
        this.addInnerBuilder(modifiersBuilder);
        this.addInnerBuilder(nameBuilder);
    }

    /**
     * �o�^���ꂽ�X�e�[�g�}�l�[�W�������ԕω����󂯎�郁�\�b�h�D
     * @param event ��ԕω��C�x���g
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder#stateChanged(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent)
     */
    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType type = event.getType();
        if (type.equals(CLASS_STATE_CHANGE.ENTER_CLASS_DEF)) {
            //�N���X��`�ɓ�������C�Ƃ肠�����V�����N���X��`��o�^���ɍs��
            final AstVisitEvent trigger = event.getTrigger();
            this.startClassDefinition(event, trigger.getStartLine(), trigger.getStartColumn(),
                    trigger.getEndLine(), trigger.getEndColumn());

        } else if (type.equals(CLASS_STATE_CHANGE.EXIT_CLASS_DEF)) {
            //�N���X��`������o���̂ŁC�f�[�^�\�z���I�����Č�n�����s���D
            this.endClassDefinition();
            this.isClassNameBuit = false;
        } else if (type.equals(CLASS_STATE_CHANGE.ENTER_CLASS_BLOCK)) {
            //�N���X�̃u���b�N�ɓ������̂Ńf�[�^�Ǘ��҂ɒʒm���āC���O�\�z�t���O���~�낷
            this.isClassNameBuit = false;
            this.enterClassblock();
        } else if (this.classStateManager.isInPreDeclaration()) {
            //�N���X�u���b�N�̑O�́C�N���X�錾���ɋ���ꍇ�D
            if (type.equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.ENTER_MODIFIERS_DEF)) {
                //�C���q��`���ɓ������̂ŏC���q�r���_���A�N�e�B�u��
                this.modifiersBuilder.activate();
            } else if (type
                    .equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.EXIT_MODIFIERS_DEF)) {
                //�C���q��`������o���̂ŏC���q�r���_���A�N�e�B�u�ɂ��ďC���q����o�^����
                this.modifiersBuilder.deactivate();
                this.registModifiers(this.modifiersBuilder.popLastBuiltData());
                this.modifiersBuilder.clearBuiltData();
            } else if (type.equals(NameStateManager.NAME_STATE.ENTER_NAME) && !this.isClassNameBuit) {
                //���O����`���ɍŏ��ɓ��������ɃN���X�����\�z����
                this.nameBuilder.activate();
            } else if (type.equals(NameStateManager.NAME_STATE.EXIT_NAME) && !this.isClassNameBuit) {
                //�ŏ��ɖ��O����`������o�����ɃN���X����o�^���āC�N���X�����\�z�ς݂ł��邱�Ƃ�\���t���O�𗧂Ă�
                this.nameBuilder.deactivate();
                this.registClassName(this.nameBuilder.getFirstBuiltData());
                this.nameBuilder.clearBuiltData();
                this.isClassNameBuit = true;
            }
        }
    }

    /**
     * �N���X��`������o�����ɌĂяo����C
     * �f�[�^�Ǘ��҂� {@link BuildDataManager#endClassDefinition()} ���\�b�h��ʂ��Ă����ʒm���C
     * �\�z���̃f�[�^������X�^�b�N������o���D
     * 
     * ���̃��\�b�h���I�[�o�[���C�h���邱�ƂŁC
     * �N���X��`�����I�������Ƃ��̏�����C�ӂɕύX���邱�Ƃ��ł���D
     */
    protected void endClassDefinition() {
        this.buildManager.endClassDefinition();
        if (!this.buildingClassStack.isEmpty()) {
            this.buildingClassStack.pop();
        }
    }

    /**
     * �N���X�u���b�N�ɓ��������ɌĂяo����C
     * �f�[�^�\�z���ɂ��̎|��ʒm����D
     * 
     * ���̃��\�b�h���I�[�o�[���C�h���邱�ƂŁC
     * �N���X�u���b�N�ɓ�����������C�ӂɕύX���邱�Ƃ��ł���D
     */
    protected void enterClassblock() {
        this.buildManager.enterClassBlock();
    }

    /**
     * �\�z���̃N���X��񂪂���΂�����C�Ȃ����null��Ԃ��D
     * 
     * @return �\�z���̃N���X��񂪂���΂�����C�Ȃ����null�D
     */
    protected final UnresolvedClassInfo getBuildingClassInfo() {
        if (this.hasBuildingClassInfo()) {
            return this.buildingClassStack.peek();
        } else {
            return null;
        }
    }

    /**
     * �\�z���̃N���X��񂪂��邩�ǂ�����Ԃ��D
     * 
     * @return �\�z���̃N���X��񂪂����true
     */
    protected boolean hasBuildingClassInfo() {
        return !this.buildingClassStack.isEmpty();
    }

    /**
     * �N���X��`���ɓ��������ɌĂ΂�郁�\�b�h�D
     * �V�������B�����N���X�̃C���X�^���X�𐶐����C�o�^���邽�߂̃��\�b�h{@link #registClassInfo(UnresolvedClassInfo)}���ĂԁD
     * 
     * ���̃��\�b�h���I�[�o�[���C�h���邱�ƂŁC�N���X��`���ɓ������ۂ̋�����C�ӂɕύX���邱�Ƃ��ł���D
     * 
     * @param startLine �N���X�̊J�n�s�ԍ�
     * @param startColumn �N���X�̊J�n��ԍ�
     * @param endLine�@�N���X�̏I���s�ԍ�
     * @param endColumn�@�N���X�̏I����ԍ�
     */
    protected void startClassDefinition(final StateChangeEvent<AstVisitEvent> event,
            final int startLine, final int startColumn, final int endLine, final int endColumn) {
        final FileInfo currentFile = DataManager.getInstance().getFileInfoManager().getCurrentFile(
                Thread.currentThread());
        assert null != currentFile : "Illegal state: the file information was not registered to FileInfoManager";

        final UnresolvedClassInfo classInfo = new UnresolvedClassInfo(currentFile,
                this.buildManager.getCurrentUnit());

        //enum�錾�������ꍇ��enum�ł��邱�Ƃ�o�^
        if (event.getTrigger().getToken().isEnumDefinition()) {
            classInfo.setIsEnum();
        }

        classInfo.setFromLine(startLine);
        classInfo.setFromColumn(startColumn);
        classInfo.setToLine(endLine);
        classInfo.setToColumn(endColumn);

        this.buildingClassStack.push(classInfo);
        this.registClassInfo(classInfo);
    }

    /**
     * �N���X��`���ɓ���C�V�����N���X�����쐬�����Ƃ��ɌĂяo����C
     * �f�[�^�Ǘ��҂ɑ΂��ĐV�����N���X����o�^����D
     * �܂����̏������̃N���X�̓����X�^�b�N�ɂ��ۑ�����D
     * 
     * ���̃��\�b�h���I�[�o�[���C�h���邱�ƂŁC�N���X���̓o�^������C�ӂɕύX���邱�Ƃ��ł���D
     * 
     * @param classInfo �o�^����V�K�N���X
     */
    protected void registClassInfo(final UnresolvedClassInfo classInfo) {
        this.registBuiltData(classInfo);
        this.buildManager.startClassDefinition(classInfo);
    }

    /**
     * �N���X����񂪍\�z���ꂽ���ɌĂяo����C���ݍ\�z���̃N���X���ɑ΂��āC���O��o�^����D
     * �\�z���̃N���X�f�[�^�������X�^�b�N���甭���ł��Ȃ��ꍇ�́C�������Ȃ��D
     * 
     * ���̃��\�b�h��C�ӂɃI�[�o�[���C�h���邱�ƂŁC���O���̓o�^������C�ӂɕύX���邱�Ƃ��ł���D
     * 
     * @param name �\�z���̃N���X���D
     */
    protected void registClassName(final String[] name) {
        if (this.hasBuildingClassInfo() && 0 < name.length) {
            final int classStackSize = this.buildingClassStack.size();

            if (classStackSize > 1
                    && !(this.buildManager.getOuterUnit() instanceof UnresolvedClassInfo)) {

                // �O�������\�b�h�̃C���i�[�N���X�̏ꍇ�C�O���̃N���X�����猩�ĉ��Ԗڂ̃C���i�[�N���X��������ID��U��
                final int id = this.buildingClassStack.get(classStackSize - 2).getInnerClasses()
                        .size();
                name[0] = (id + 1) + name[0];
            }
            this.getBuildingClassInfo().setClassName(name[0]);
        }
    }

    /**
     * �N���X�̏C���q��񂪍\�z���ꂽ���ɌĂяo����C�C���q����o�^����D
     * 
     * ���̃��\�b�h���I�[�o�[���C�h���邱�ƂŁC�C���q���画�f�������̓o�^������C�ӂɕύX���邱�Ƃ��ł���D
     * 
     * @param modifiers�@�N���X�ɕt����ꂽ�C���q�̔z��
     */
    protected void registModifiers(final ModifierInfo[] modifiers) {
        if (this.hasBuildingClassInfo()) {
            final UnresolvedClassInfo buildingclass = this.getBuildingClassInfo();
            for (final ModifierInfo modifier : modifiers) {
                buildingclass.addModifier(modifier);
            }

        }
    }

    /**
     * �N���X��`���Ɋ֘A����r�W�^�[�̏�Ԃ��Ǘ����C��ԕω��C�x���g��ʒm����D
     */
    private final ClassDefinitionStateManager classStateManager = new ClassDefinitionStateManager();

    /**
     * �\�z���f�[�^�̊Ǘ���
     */
    private final BuildDataManager buildManager;

    /**
     * �C���q�����\�z����r���_
     */
    private final ModifiersBuilder modifiersBuilder;

    /**
     * ���O�����\�z����r���_
     */
    private final NameBuilder nameBuilder;

    /**
     * �\�z���̃N���X�f�[�^�X�^�b�N
     */
    private final Stack<UnresolvedClassInfo> buildingClassStack = new Stack<UnresolvedClassInfo>();

    /**
     * �\�z���̃N���X�f�[�^�ɑ΂��ăN���X�������łɍ\�z���ꂽ���ǂ�����\��
     */
    private boolean isClassNameBuit = false;
}
