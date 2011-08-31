package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ���Z�q��\���񋓌^
 * 
 * @author higo
 * 
 */
public enum OPERATOR_TYPE {

    /**
     * �Z�p���Z�q��\���萔�D ��̓I�ɂ́C+, -, *, /, %�D
     */
    ARITHMETIC,

    /**
     * ��r���Z�q��\���萔�D ��̓I�ɂ́C<, >, <=, >=, ==, !=.
     */
    COMPARATIVE,

    /**
     * �_�����Z�q��\���萔�D ��̓I�ɂ́C&&, ||, !.
     */
    LOGICAL,

    /**
     * �r�b�g���Z�q��\���萔�D ��̓I�ɂ́C&, |, ^, ~�D
     */
    BITS,

    /**
     * �V�t�g�i�r�b�g����j���Z�q��\���萔�D ��̓I�ɂ́C>>, >>>, <<.
     */
    SHIFT,

    /**
     * ������Z�q��\���萔�D = �����łȂ��C+= �� -= �ȂǍ��ӂɑ�������ϐ���u���S�Ẳ��Z�q��\���D
     */
    ASSIGNMENT;
    
}
