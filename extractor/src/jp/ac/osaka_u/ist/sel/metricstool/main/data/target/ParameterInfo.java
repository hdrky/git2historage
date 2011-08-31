package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * ������\�����߂̃N���X�D �^��񋟂���̂݁D
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public abstract class ParameterInfo extends VariableInfo<CallableUnitInfo> {

    /**
     * �����I�u�W�F�N�g������������D���O�ƌ^���K�v�D
     * 
     * @param modifiers �C���q�� Set
     * @param name ������
     * @param type �����̌^
     * @param definitionMethod �錾���Ă��郁�\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    ParameterInfo(final Set<ModifierInfo> modifiers, final String name, final TypeInfo type,
            final CallableUnitInfo definitionMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(modifiers, name, type, definitionMethod, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * �^����ꂽ�ϐ���Set�Ɋ܂܂�Ă��������Set�Ƃ��ĕԂ�
     * @param variables �ϐ���Set
     * @return �^����ꂽ�ϐ���Set�Ɋ܂܂�������Set
     */
    public static Set<ParameterInfo> getLocalVariables(Collection<VariableInfo<?>> variables) {
        final Set<ParameterInfo> parameters = new HashSet<ParameterInfo>();
        for (final VariableInfo<?> variable : variables) {
            if (variable instanceof ParameterInfo) {
                parameters.add((ParameterInfo) variable);
            }
        }
        return Collections.unmodifiableSet(parameters);
    }
}
