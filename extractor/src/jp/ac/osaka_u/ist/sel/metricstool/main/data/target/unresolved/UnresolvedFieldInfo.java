package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticOrInstance;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Visualizable;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * AST�p�[�X�Ŏ擾�����t�B�[���h�����ꎞ�I�Ɋi�[���邽�߂̃N���X�D
 * 
 * 
 * @author higo
 * 
 */
public final class UnresolvedFieldInfo extends
        UnresolvedVariableInfo<TargetFieldInfo, UnresolvedClassInfo> implements Visualizable,
        StaticOrInstance {

    /**
     * Unresolved�t�B�[���h�I�u�W�F�N�g������������D �t�B�[���h���ƌ^�C��`���Ă���N���X���^�����Ȃ���΂Ȃ�Ȃ��D
     * 
     * @param name �t�B�[���h��
     * @param type �t�B�[���h�̌^
     * @param definitionClass �t�B�[���h���`���Ă���N���X
     * @param initializer �t�B�[���h�̏�������
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public UnresolvedFieldInfo(final String name, final UnresolvedTypeInfo<?> type,
            final UnresolvedClassInfo definitionClass,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> initializer,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(name, type, definitionClass, fromLine, fromColumn, toLine, toColumn);

        if (null == definitionClass) {
            throw new NullPointerException();
        }

        this.ownerClass = definitionClass;
        this.initializer = initializer;
    }

    @Override
    public TargetFieldInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // ���L�N���X������
        final UnresolvedClassInfo unresolvedOwnerClass = this.getOwnerClass();
        final TargetClassInfo ownerClass = unresolvedOwnerClass.resolve(null, null,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // �C���q�C���O�C�����C�C���X�^���X�����o�[���ǂ������擾
        // �^�݂̂����ł͉������Ȃ�
        final Set<ModifierInfo> modifiers = this.getModifiers();
        final String fieldName = this.getName();
        final boolean instance = this.isInstanceMember();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new TargetFieldInfo(modifiers, fieldName, ownerClass, instance,
                fromLine, fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }

    public TargetFieldInfo resolveType(final ClassInfoManager classInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        final TargetFieldInfo resolved = this.getResolved();
        final TargetClassInfo ownerClass = this.getOwnerClass().getResolved();

        final UnresolvedTypeInfo<?> unresolvedType = this.getType();
        final TypeInfo type = unresolvedType
                .resolve(ownerClass, null, classInfoManager, null, null);
        resolved.setType(type);

        return resolved;
    }

    /**
     * ���̃t�B�[���h���`���Ă��関�����N���X����Ԃ�
     * 
     * @return ���̃t�B�[���h���`���Ă��関�����N���X���
     */
    public UnresolvedClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * ���̃t�B�[���h���`���Ă��関�����N���X�����Z�b�g����
     * 
     * @param ownerClass ���̃t�B�[���h���`���Ă��関�����N���X���
     */
    public void setOwnerClass(final UnresolvedClassInfo ownerClass) {

        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
    }

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �q�N���X����Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public boolean isInheritanceVisible() {
        final UnresolvedClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isInheritanceVisible(this
                .getModifiers());
    }

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �������O��Ԃ���Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public boolean isNamespaceVisible() {
        final UnresolvedClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isNamespaceVisible(this
                .getModifiers());
    }

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �ǂ�����ł��Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public boolean isPublicVisible() {
        final UnresolvedClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isPublicVisible(getModifiers());
    }

    /**
     * �C���X�^���X�����o�[���ǂ�����Ԃ�
     * 
     * @return �C���X�^���X�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    @Override
    public boolean isInstanceMember() {
        final UnresolvedClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? false : ModifierInfo
                .isInstanceMember(this.getModifiers());
    }

    /**
     * �X�^�e�B�b�N�����o�[���ǂ�����Ԃ�
     * 
     * @return �X�^�e�B�b�N�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    @Override
    public boolean isStaticMember() {
        final UnresolvedClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isStaticMember(this.getModifiers());
    }

    /**
     * �ϐ��̏���������Ԃ�
     * 
     * @return �ϐ��̏��������D����������Ă��Ȃ��ꍇ��null
     */
    public final UnresolvedExpressionInfo<? extends ExpressionInfo> getInitilizer() {
        return this.initializer;
    }

    /**
     * ���̃t�B�[���h���`���Ă���N���X��ۑ����邽�߂̕ϐ�
     */
    private UnresolvedClassInfo ownerClass;

    /**
     * �ϐ��̏���������\���ϐ�
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> initializer;

}
