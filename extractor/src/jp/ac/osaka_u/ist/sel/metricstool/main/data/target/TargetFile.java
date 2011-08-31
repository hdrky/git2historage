package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 
 * @author higo
 * 
 * �Ώۃt�@�C���̃f�[�^���i�[����N���X
 * 
 * since 2006.11.12
 */
public final class TargetFile implements Comparable<TargetFile> {

    /**
     * 
     * @param name �Ώۃt�@�C���̃p�X
     * 
     * �Ώۃt�@�C���̃p�X��p���ď�����
     */
    public TargetFile(final String name) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new NullPointerException();
        }

        this.correctSyntax = false;
        this.name = name;
    }

    /**
     * ���̃I�u�W�F�N�g�ƑΏۃI�u�W�F�N�g�̏����֌W��Ԃ��D
     * 
     * @param targetFile ��r�ΏۃI�u�W�F�N�g
     * @return �����֌W
     */
    public int compareTo(final TargetFile targetFile) {

        if (null == targetFile) {
            throw new NullPointerException();
        }

        String name = this.getName();
        String correspondName = targetFile.getName();
        return name.compareTo(correspondName);
    }

    /**
     * 
     * @param o ��r�Ώۃt�@�C��
     * @return ���̑Ώۃt�@�C���Ɣ�r�Ώۃt�@�C���̃t�@�C���p�X���������ꍇ�� true�C�����łȂ���� false
     * 
     * ���̑Ώۃt�@�C���̃t�@�C���p�X�ƁC�����ŗ^����ꂽ�Ώۃt�@�C���̃p�X�������������`�F�b�N����D�������ꍇ�� true ��Ԃ��C�����łȂ��ꍇ�� false ��Ԃ��D
     * 
     */
    @Override
    public boolean equals(Object o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (!(o instanceof TargetFile)) {
            return false;
        }

        String thisName = this.getName();
        String correspondName = ((TargetFile) o).getName();
        return thisName.equals(correspondName);
    }

    /**
     * 
     * @return �Ώۃt�@�C���̃p�X
     * 
     * �Ώۃt�@�C���̃p�X��Ԃ�
     */
    public final String getName() {
        return this.name;
    }

    /**
     * �Ώۃt�@�C���̃n�b�V���R�[�h��Ԃ�
     * 
     * @return �Ώۃt�@�C���̃n�b�V���R�[�h
     * 
     */
    @Override
    public int hashCode() {
        String name = this.getName();
        return name.hashCode();
    }

    /**
     * �Ώۃt�@�C�������@������������Ԃ�
     * 
     * @return ���@���������ꍇ�� true, �������Ȃ��ꍇ�� false
     */
    public boolean isCorrectSyntax() {
        return this.correctSyntax;
    }

    /**
     * �Ώۃt�@�C���̕��@�����������ǂ�����ۑ�����
     * 
     * @param correctSyntax �Ώۃt�@�C���̕��@�̐������D�������ꍇ�� true�C�������Ȃ��ꍇ�� false
     */
    public void setCorrectSytax(final boolean correctSyntax) {
        this.correctSyntax = correctSyntax;
    }

    /**
     * �Ώۃt�@�C���̍\��������������ۑ����邽�߂̕ϐ�
     */
    private boolean correctSyntax;

    /**
     * 
     * �Ώۃt�@�C���̃p�X��ۑ����邽�߂̕ϐ�
     */
    private final String name;

}
