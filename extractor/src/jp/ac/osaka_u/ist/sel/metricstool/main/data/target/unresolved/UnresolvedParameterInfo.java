package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������\�����߂̃N���X�D �^��񋟂���̂݁D
 * 
 * @author higo
 * 
 */
public class UnresolvedParameterInfo
        extends
        UnresolvedVariableInfo<TargetParameterInfo, UnresolvedCallableUnitInfo<? extends CallableUnitInfo>> {

    /**
     * �����I�u�W�F�N�g������������D���O�ƌ^���K�v�D
     * 
     * @param name ������
     * @param type �����̌^
     * @param index ���Ԗڂ̈����ł邩��\��
     * @param definitionMethod ������錾���Ă��郁�\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public UnresolvedParameterInfo(final String name, final UnresolvedTypeInfo<?> type,
            final int index,
            final UnresolvedCallableUnitInfo<? extends CallableUnitInfo> definitionMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(name, type, definitionMethod, fromLine, fromColumn, toLine, toColumn);

        this.index = index;
    }

    /**
     * ���������������������C�����ςݎQ�Ƃ�Ԃ��D
     * 
     * @param usingClass �������������̒�`���s���Ă���N���X
     * @param usingMethod �������������̒�`���s���Ă��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @return �����ς݈������
     */
    @Override
    public TargetParameterInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final UnresolvedCallableUnitInfo<?> unresolvedOwnerMethod = this.getDefinitionUnit();
        final CallableUnitInfo ownerMethod = unresolvedOwnerMethod.resolve(null, null,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final UnresolvedClassInfo unresolvedOwnerClass = unresolvedOwnerMethod.getOwnerClass();
        final TargetClassInfo ownerClass = unresolvedOwnerClass.resolve(null, null,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // �C���q�C�p�����[�^���C�^�C�ʒu�����擾
        final Set<ModifierInfo> modifiers = this.getModifiers();
        final String name = this.getName();
        final int index = this.getIndex();
        final UnresolvedTypeInfo<?> unresolvedParameterType = this.getType();
        final TypeInfo type = unresolvedParameterType.resolve(ownerClass, ownerMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final int parameterFromLine = this.getFromLine();
        final int parameterFromColumn = this.getFromColumn();
        final int parameterToLine = this.getToLine();
        final int parameterToColumn = this.getToColumn();

        final CallableUnitInfo ownerCallableUnit = this.getDefinitionUnit().getResolved();

        // �p�����[�^�I�u�W�F�N�g�𐶐�����
        this.resolvedInfo = new TargetParameterInfo(modifiers, name, type, index,
                ownerCallableUnit, parameterFromLine, parameterFromColumn, parameterToLine,
                parameterToColumn);
        return this.resolvedInfo;
    }

    /**
     * �����̃C���f�b�N�X��Ԃ�
     * 
     * @return�@�����̃C���f�b�N�X
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * �����̃C���f�b�N�X��ۑ����邽�߂̕ϐ�
     */
    private final int index;

}
