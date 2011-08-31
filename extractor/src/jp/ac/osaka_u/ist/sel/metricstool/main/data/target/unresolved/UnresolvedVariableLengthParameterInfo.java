package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetVariableLengthParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableLengthParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class UnresolvedVariableLengthParameterInfo extends UnresolvedParameterInfo implements
        VariableLengthParameterInfo {

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
    public UnresolvedVariableLengthParameterInfo(final String name,
            final UnresolvedTypeInfo<?> type, final int index,
            final UnresolvedCallableUnitInfo<? extends CallableUnitInfo> definitionMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(name, UnresolvedArrayTypeInfo.getType(type, 1), index, definitionMethod, fromLine,
                fromColumn, toLine, toColumn);
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

        final TargetParameterInfo resolvedParameter = super.resolve(null, null, classInfoManager,
                fieldInfoManager, methodInfoManager);

        final Set<ModifierInfo> modifiers = resolvedParameter.getModifiers();
        final String name = resolvedParameter.getName();
        final TypeInfo type = ((ArrayTypeInfo) resolvedParameter.getType()).getElementType();
        final int index = resolvedParameter.getIndex();
        final CallableUnitInfo definitionUnit = resolvedParameter.getDefinitionUnit();
        final int fromLine = resolvedParameter.getFromLine();
        final int fromColumn = resolvedParameter.getFromColumn();
        final int toLine = resolvedParameter.getToLine();
        final int toColumn = resolvedParameter.getToColumn();

        this.resolvedInfo = new TargetVariableLengthParameterInfo(modifiers, name, type, index,
                definitionUnit, fromLine, fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }
}
