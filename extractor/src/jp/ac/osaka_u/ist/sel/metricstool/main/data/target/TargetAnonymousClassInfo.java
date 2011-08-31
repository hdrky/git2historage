package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;


/**
 * �����C���i�[�N���X�������N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class TargetAnonymousClassInfo extends TargetInnerClassInfo implements
        AnonymousClassInfo {

    /**
     * �����C���i�[�N���X�I�u�W�F�N�g������������
     * 
     * @param namespace ���O���
     * @param className �N���X��
     * @param fileInfo ���̃N���X��錾���Ă���t�@�C�����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetAnonymousClassInfo(final NamespaceInfo namespace, final String className,
            final FileInfo fileInfo, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(new HashSet<ModifierInfo>(), namespace, className, false, fileInfo, fromLine,
                fromColumn, toLine, toColumn);
    }

    /**
     * �����C���i�[�N���X�I�u�W�F�N�g������������
     * 
     * @param fullQualifiedName ���S���薼
     * @param fileInfo ���̃N���X��錾���Ă���t�@�C�����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetAnonymousClassInfo(final String[] fullQualifiedName, final FileInfo fileInfo,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(new HashSet<ModifierInfo>(), fullQualifiedName, false, fileInfo, fromLine,
                fromColumn, toLine, toColumn);
    }
}
