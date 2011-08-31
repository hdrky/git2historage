package jp.ac.osaka_u.ist.sel.metricstool.main.util;


/**
 * �������\��Enum�D
 * 
 * ���݂�JAVA�̂�(2006/11/16)
 * 
 * @author kou-tngt
 * 
 */
public enum LANGUAGE {
    /**
     * Java���� �o�[�W����1.5�ȍ~�p
     */
    JAVA15 {
        @Override
        public boolean isObjectOrientedLanguage() {
            return true;
        }

        @Override
        public String getExtension() {
            return ".java";
        }

        @Override
        public String getName() {
            return "Java";
        }

        @Override
        public String getIdentifierName() {
            return "java15";
        }

        @Override
        public String getNamespaceDelimiter() {
            return ".";
        }
    },

    /**
     * Java���� �o�[�W����1.4�p
     */
    JAVA14 {
        @Override
        public boolean isObjectOrientedLanguage() {
            return true;
        }

        @Override
        public String getExtension() {
            return ".java";
        }

        @Override
        public String getName() {
            return "Java";
        }

        @Override
        public String getIdentifierName() {
            return "java14";
        }

        @Override
        public String getNamespaceDelimiter() {
            return ".";
        }
    },

    /**
     * Java���� �o�[�W����1.3�p
     */
    JAVA13 {
        @Override
        public boolean isObjectOrientedLanguage() {
            return true;
        }

        @Override
        public String getExtension() {
            return ".java";
        }

        @Override
        public String getName() {
            return "Java";
        }

        @Override
        public String getIdentifierName() {
            return "java13";
        }

        @Override
        public String getNamespaceDelimiter() {
            return ".";
        }
    },
    
    /**
     * Java���� �o�[�W����1.3�p
     */
    CSHARP {
        @Override
        public boolean isObjectOrientedLanguage() {
            return true;
        }

        @Override
        public String getExtension() {
            return ".cs";
        }

        @Override
        public String getName() {
            return "CSharp";
        }

        @Override
        public String getIdentifierName() {
            return "CSharp";
        }

        @Override
        public String getNamespaceDelimiter() {
            return ".";
        }
    },

    //    C_PLUS_PLUS {
    //        @Override
    //        public boolean isObjectOrientedLanguage() {
    //            return true;
    //        }
    //    },

    //    C_SHARP {
    //        @Override
    //        public boolean isObjectOrientedLanguage() {
    //            return true;
    //        }
    //    },

    ;

    /**
     * ���̌��ꂪ�I�u�W�F�N�g�w�����ꂩ�ǂ�����Ԃ����\�b�h
     * 
     * @return �I�u�W�F�N�g�w������ł����true
     */
    public boolean isObjectOrientedLanguage() {
        return false;
    }

    /**
     * ���̌��ꂪ�\�������ꂩ�ǂ�����Ԃ����\�b�h
     * 
     * @return �\��������ł����true
     */
    public boolean isStructuralLanguage() {
        return false;
    }

    /**
     * ���̌��ꂪ�֐��^���ꂩ�ǂ�����Ԃ����\�b�h
     * 
     * @return �֐��^����ł����true
     */
    public boolean isFunctionalLanguage() {
        return false;
    }

    /**
     * ���̌��ꂪ�X�N���v�g���ꂩ�ǂ�����Ԃ����\�b�h
     * 
     * @return �X�N���v�g����ł����true
     */
    public boolean isScriptLanguage() {
        return false;
    }

    /**
     * ���̌���̊g���q��Ԃ�
     * 
     * @return ���̌���̊g���q
     */
    public abstract String getExtension();

    /**
     * ���̌���̖��O��Ԃ�
     * 
     * @return ���̌���̖��O
     * 
     */
    public abstract String getName();

    /**
     * ���̌���̎��ʎq����Ԃ��D
     * ���ʎq���� -l �I�v�V�����ɗp������.
     * 
     * @return ���̌���̐�����
     */
    public abstract String getIdentifierName();

    /**
     * ���̌���̖��O��Ԃ���؂镶���i��j����Ԃ��D
     * 
     * @return ���̌���̖��O��Ԃ���؂镶���i��j
     */
    public abstract String getNamespaceDelimiter();
}
