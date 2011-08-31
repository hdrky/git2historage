package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.io.File;
import java.security.AccessControlException;
import java.security.Permission;
import java.security.Permissions;
import java.util.Enumeration;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.ClassInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.ClassMetricsRegister;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.DefaultClassInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.DefaultClassMetricsRegister;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.DefaultFieldMetricsRegister;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.DefaultFileInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.DefaultFileMetricsRegister;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.DefaultMethodInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.DefaultMethodMetricsRegister;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.FieldMetricsRegister;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.FileInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.FileMetricsRegister;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.MethodInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.MethodMetricsRegister;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.AlreadyConnectedException;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultProgressReporter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.ProgressReporter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.ProgressSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * ���g���N�X�v���v���O�C�������p�̒��ۃN���X
 * <p>
 * �e�v���O�C���͂��̃N���X���p�������N���X��1�����Ȃ���΂Ȃ�Ȃ��D �܂��C���̃N���X����plugin.xml�t�@�C���Ɏw��̌`���ŋL�q���Ȃ���΂Ȃ�Ȃ��D
 * <p>
 * main���W���[���͊e�v���O�C���f�B���N�g������plugin.xml�t�@�C����T�����A �����ɋL�q����Ă���C���̃N���X���p�������N���X���C���X�^���X�����A
 * �e���\�b�h��ʂ��ď����擾������Aexecute���\�b�h���Ăяo���ă��g���N�X�l���v������
 * 
 * @author kou-tngt
 */
public abstract class AbstractPlugin implements MessageSource, ProgressSource {

    /**
     * �v���O�C���̏���ۑ���������s�σN���X�D AbstractPlugin����̂݃C���X�^���X���ł���D
     * <p>
     * �v���O�C���̏��𓮓I�ɕύX�����ƍ���̂ŁA���̓����N���X�̃C���X�^���X��p���� �������Ƃ��邱�ƂŃv���O�C�����̕s�ϐ�����������D
     * �e�v���O�C���̏���ۑ�����PluginInfo�C���X�^���X�̎擾�ɂ� {@link AbstractPlugin#getPluginInfo()}��p����D
     * 
     * @author kou-tngt
     * 
     */
    public class PluginInfo {

        /**
         * �f�t�H���g�̃R���X�g���N�^
         */
        private PluginInfo() {
            final LANGUAGE[] languages = AbstractPlugin.this.getMeasurableLanguages();
            this.measurableLanguages = new LANGUAGE[languages.length];
            System.arraycopy(languages, 0, this.measurableLanguages, 0, languages.length);
            this.metricName = AbstractPlugin.this.getMetricName();
            this.metricType = AbstractPlugin.this.getMetricType();
            this.useClassInfo = AbstractPlugin.this.useClassInfo();
            this.useMethodInfo = AbstractPlugin.this.useMethodInfo();
            this.useFieldInfo = AbstractPlugin.this.useFieldInfo();
            this.useFileInfo = AbstractPlugin.this.useFileInfo();
            this.useMethodLocalInfo = AbstractPlugin.this.useMethodLocalInfo();
            this.description = AbstractPlugin.this.getDescription();
            this.detailDescription = AbstractPlugin.this.getDetailDescription();
        }

        /**
         * ���̃v���O�C���̊ȈՐ������P�s�ŕԂ��i�ł���Ήp��Łj. �f�t�H���g�̎����ł� "Measure ���g���N�X�� metrics." �ƕԂ�
         * �e�v���O�C���͂��̃��\�b�h��C�ӂɃI�[�o�[���C�h����.
         * 
         * @return �ȈՐ���������
         */
        public String getDescription() {
            return this.description;
        }

        /**
         * ���̃v���O�C���̏ڍא�����Ԃ��i�ł���Ήp��Łj. �f�t�H���g�̎����ł͋󕶎����Ԃ� �e�v���O�C���͂��̃��\�b�h��C�ӂɃI�[�o�[���C�h����.
         * 
         * @return �ڍא���������
         */
        public String getDetailDescription() {
            return this.detailDescription;
        }

        /**
         * ���̃v���O�C�������g���N�X���v���ł��錾���Ԃ��D
         * 
         * @return �v���\�Ȍ����S�Ċ܂ޔz��D
         */
        public LANGUAGE[] getMeasurableLanguages() {
            return this.measurableLanguages;
        }

        /**
         * ���̃v���O�C���������Ŏw�肳�ꂽ����ŗ��p�\�ł��邩��Ԃ��D
         * 
         * @param language ���p�\�ł��邩�𒲂ׂ�������
         * @return ���p�\�ł���ꍇ�� true�C���p�ł��Ȃ��ꍇ�� false�D
         */
        public boolean isMeasurable(final LANGUAGE language) {
            final LANGUAGE[] measurableLanguages = this.getMeasurableLanguages();
            for (int i = 0; i < measurableLanguages.length; i++) {
                if (language.equals(measurableLanguages[i])) {
                    return true;
                }
            }
            return false;
        }

        /**
         * ���̃v���O�C�����v�����郁�g���N�X�̖��O��Ԃ��D
         * 
         * @return ���g���N�X��
         */
        public String getMetricName() {
            return this.metricName;
        }

        /**
         * ���̃v���O�C�����v�����郁�g���N�X�̃^�C�v��Ԃ��D
         * 
         * @return ���g���N�X�^�C�v
         * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE
         */
        public METRIC_TYPE getMetricType() {
            return this.metricType;
        }

        /**
         * ���̃v���O�C�����N���X�Ɋւ�����𗘗p���邩�ǂ�����Ԃ��D
         * 
         * @return �N���X�Ɋւ�����𗘗p����ꍇ��true�D
         */
        public boolean isUseClassInfo() {
            return this.useClassInfo;
        }

        /**
         * ���̃v���O�C�����t�B�[���h�Ɋւ�����𗘗p���邩�ǂ�����Ԃ��D
         * 
         * @return �t�B�[���h�Ɋւ�����𗘗p����ꍇ��true�D
         */
        public boolean isUseFieldInfo() {
            return this.useFieldInfo;
        }

        /**
         * ���̃v���O�C�����t�@�C���Ɋւ�����𗘗p���邩�ǂ�����Ԃ��D
         * 
         * @return �t�@�C���Ɋւ�����𗘗p����ꍇ��true�D
         */
        public boolean isUseFileInfo() {
            return this.useFileInfo;
        }

        /**
         * ���̃v���O�C�������\�b�h�Ɋւ�����𗘗p���邩�ǂ�����Ԃ��D
         * 
         * @return ���\�b�h�Ɋւ�����𗘗p����ꍇ��true�D
         */
        public boolean isUseMethodInfo() {
            return this.useMethodInfo;
        }

        /**
         * ���̃v���O�C�������\�b�h�����Ɋւ�����𗘗p���邩�ǂ�����Ԃ��D
         * 
         * @return ���\�b�h�����Ɋւ�����𗘗p����ꍇ��true�D
         */
        public boolean isUseMethodLocalInfo() {
            return this.useMethodLocalInfo;
        }

        private final LANGUAGE[] measurableLanguages;

        private final String metricName;

        private final METRIC_TYPE metricType;

        private final String description;

        private final String detailDescription;

        private final boolean useClassInfo;

        private final boolean useFieldInfo;

        private final boolean useFileInfo;

        private final boolean useMethodInfo;

        private final boolean useMethodLocalInfo;
    }

    /**
     * �v���O�C���̎��s���ɋ������p�[�~�b�V������ǉ�����. ���ʌ��������X���b�h���炵���Ăяo���Ȃ�.
     * 
     * @param permission ������p�[�~�b�V����
     * @throws AccessControlException ���ʌ����������Ȃ��X���b�h����Ăяo�����ꍇ
     */
    public final void addPermission(final Permission permission) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.permissions.add(permission);
    }

    /**
     * �v���O�C���C���X�^���X���m���r����. �N���X�̕W����������Ȃ炻���p���Ĕ�r����. ���Ȃ��ꍇ�́C {@link Class}�C���X�^���X�̂��r����.
     * �������C�ʏ�̋@�\��p���ă��[�h�����v���O�C���������N���X�ł��邱�Ƃ͂��肦�Ȃ�.
     * ����āC����v���O�C���N���X�̃C���X�^���X�͕ʂ̃N���X���[�_���烍�[�h����Ă�����ł���Ɣ��肳���.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     * @see #hashCode()
     */
    @Override
    public final boolean equals(final Object o) {
        if (o instanceof AbstractPlugin) {
            final String myClassName = this.getClass().getCanonicalName();
            final String otherClassName = o.getClass().getCanonicalName();
            if (null != myClassName && null != otherClassName) {
                // �ǂ���������N���X����Ȃ��ꍇ
                return myClassName.equals(otherClassName);
            } else if (null != myClassName || null != otherClassName) {
                // �ǂ������͓����N���X�����ǁC�ǂ������͈Ⴄ
                return false;
            } else {
                // �����Ƃ������N���X
                return this.getClass().equals(o.getClass());
            }
        }

        return false;
    }

    /**
     * �v���O�C���C���X�^���X�̃n�b�V���R�[�h��Ԃ�. �N���X�̕W����������Ȃ炻�̃n�b�V���R�[�h���g��. ���Ȃ��ꍇ�́C {@link Class}�C���X�^���X�̃n�b�V���R�[�h���g��.
     * �������C�ʏ�̋@�\��p���Ă����[�h�����v���O�C���������N���X�ł��邱�Ƃ͂��肦�Ȃ�.
     * ����āC����v���O�C���N���X�̃C���X�^���X�͕ʂ̃N���X���[�_���烍�[�h����Ă�����̃n�b�V���R�[�h��Ԃ�.
     * 
     * @see java.lang.Object#hashCode()(java.lang.Object)
     * @see #equals(Object)
     */
    @Override
    public final int hashCode() {
        final Class<?> myClass = this.getClass();
        final String myClassName = myClass.getCanonicalName();
        return myClassName != null ? myClassName.hashCode() : myClass.hashCode();
    }

    /**
     * �v���O�C���̃��[�g�f�B���N�g�����Z�b�g���� ��x�Z�b�g���ꂽ�l��ύX���邱�Ƃ͏o���Ȃ�.
     * 
     * @param rootDir ���[�g�f�B���N�g��
     * @throws NullPointerException rootDir��null�̏ꍇ
     * @throws IllegalStateException rootDir�����ɃZ�b�g����Ă���ꍇ
     */
    public final synchronized void setPluginRootdir(final File rootDir) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == rootDir) {
            throw new NullPointerException("rootdir is null.");
        }
        if (null != this.pluginRootDir) {
            throw new IllegalStateException("rootdir was already set.");
        }

        this.pluginRootDir = rootDir;
    }

    /**
     * ���b�Z�[�W���M�҂Ƃ��Ă̖��O��Ԃ�
     * 
     * @return ���M�҂Ƃ��Ă̖��O
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.plugin.connection.MessageSource#getMessageSourceName()
     */
    public String getMessageSourceName() {
        return this.sourceName;
    }

    /**
     * ���̃v���O�C���ɋ�����Ă���p�[�~�b�V�����̕s�ςȏW����Ԃ�.
     * 
     * @return ���̃v���O�C���ɋ�����Ă���p�[�~�b�V�����̏W��.
     */
    public final Permissions getPermissions() {
        final Permissions permissions = new Permissions();

        for (final Enumeration<Permission> enumeration = this.permissions.elements(); enumeration
                .hasMoreElements();) {
            permissions.add(enumeration.nextElement());
        }
        permissions.setReadOnly();
        return permissions;
    }

    /**
     * �v���O�C������ۑ����Ă���{@link PluginInfo}�N���X�̃C���X�^���X��Ԃ��D
     * �����AbstractPlugin�C���X�^���X�ɑ΂��邱�̃��\�b�h�͕K������̃C���X�^���X��Ԃ��C ���̓����ɕۑ�����Ă�����͕s�ςł���D
     * 
     * @return �v���O�C������ۑ����Ă���{@link PluginInfo}�N���X�̃C���X�^���X
     */
    public final PluginInfo getPluginInfo() {
        if (null == this.pluginInfo) {
            synchronized (this) {
                if (null == this.pluginInfo) {
                    this.pluginInfo = new PluginInfo();
                    this.sourceName = this.pluginInfo.getMetricName();
                }
            }
        }
        return this.pluginInfo;
    }

    /**
     * �v���O�C���̃��[�g�f�B���N�g����Ԃ�
     * 
     * @return �v���O�C���̃��[�g�f�B���N�g��
     */
    public final File getPluginRootDir() {
        return this.pluginRootDir;
    }

    /**
     * �i����񑗐M�҂Ƃ��Ă̖��O��Ԃ�
     * 
     * @return �i����񑗐M�҂Ƃ��Ă̖��O
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.plugin.connection.ProgressSource#getProgressSourceName()
     */
    public String getProgressSourceName() {
        return this.sourceName;
    }

    /**
     * �v���O�C����񂪊��ɍ\�z�ς݂��ǂ�����Ԃ�
     * 
     * @return �v���O�C����񂪊��ɍ\�z�ς݂Ȃ�true,�����łȂ����false
     */
    public final boolean isPluginInfoCreated() {
        return null != this.pluginInfo;
    }

    /**
     * ���g���N�X��͂��X�^�[�g���钊�ۃ��\�b�h�D
     */
    protected abstract void execute();

    /**
     * �t�@�C�����ɃA�N�Z�X����f�t�H���g�̃A�N�Z�T���擾����.
     * 
     * @return �t�@�C�����ɃA�N�Z�X����f�t�H���g�̃A�N�Z�T.
     */
    protected final FileInfoAccessor getFileInfoAccessor() {
        return this.fileInfoAccessor;
    }

    /**
     * �N���X���ɃA�N�Z�X����f�t�H���g�̃A�N�Z�T���擾����.
     * 
     * @return �N���X���ɃA�N�Z�X����f�t�H���g�̃A�N�Z�T.
     */
    protected final ClassInfoAccessor getClassInfoAccessor() {
        return this.classInfoAccessor;
    }

    /**
     * ���\�b�h���ɃA�N�Z�X����f�t�H���g�̃A�N�Z�T���擾����.
     * 
     * @return ���\�b�h���ɃA�N�Z�X����f�t�H���g�̃A�N�Z�T.
     */
    protected final MethodInfoAccessor getMethodInfoAccessor() {
        return this.methodInfoAccessor;
    }

    /**
     * ���̃v���O�C���̊ȈՐ������P�s�ŕԂ��i�ł���Ήp��Łj �f�t�H���g�̎����ł� "Measuring the ���g���N�X�� metric." �ƕԂ�
     * �e�v���O�C���͂��̃��\�b�h��C�ӂɃI�[�o�[���C�h����.
     * 
     * @return �ȈՐ���������
     */
    protected String getDescription() {
        return "Measuring the " + this.getMetricName() + " metric.";
    }

    /**
     * ���̃v���O�C���̏ڍא�����Ԃ��i�ł���Ήp��Łj �f�t�H���g�����ł͋󕶎����Ԃ�. �e�v���O�C���͂��̃��\�b�h��C�ӂɃI�[�o�[���C�h����.
     * 
     * @return
     */
    protected String getDetailDescription() {
        return "";
    }

    /**
     * ���̃v���O�C�������g���N�X���v���ł��錾���Ԃ� ���p�ł��錾��ɐ����̂���v���O�C���́A���̃��\�b�h���I�[�o�[���C�h����K�v������D
     * 
     * @return �v���\�Ȍ����S�Ċ܂ޔz��
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE
     */
    protected LANGUAGE[] getMeasurableLanguages() {
        return LANGUAGE.values();
    }

    /**
     * ���̃v���O�C�����v�����郁�g���N�X�̖��O��Ԃ����ۃ��\�b�h�D
     * 
     * @return ���g���N�X��
     */
    protected abstract String getMetricName();

    /**
     * ���̃v���O�C�����v�����郁�g���N�X�̃^�C�v��Ԃ����ۃ��\�b�h�D
     * 
     * @return ���g���N�X�^�C�v
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE
     */
    protected abstract METRIC_TYPE getMetricType();

    /**
     * �t�@�C���P�ʂ̃��g���N�X�l��o�^���郁�\�b�h.
     * 
     * @param fileInfo ���g���N�X�l��o�^����t�@�C��
     * @param value ���g���N�X�l
     * @throws MetricAlreadyRegisteredException ���ɂ��̃v���O�C�����炱�̃t�@�C���Ɋւ��郁�g���N�X�l�̕񍐂�����Ă���ꍇ.
     */
    protected final void registMetric(final FileInfo fileInfo, final Number value)
            throws MetricAlreadyRegisteredException {

        if ((null == fileInfo) || (null == value)) {
            throw new NullPointerException();
        }

        if (null == this.fileMetricsRegister) {
            synchronized (this) {
                if (null == this.fileMetricsRegister) {
                    this.fileMetricsRegister = new DefaultFileMetricsRegister(this);
                }
            }
        }
        this.fileMetricsRegister.registMetric(fileInfo, value);
    }

    /**
     * �N���X�P�ʂ̃��g���N�X�l��o�^���郁�\�b�h.
     * 
     * @param classInfo ���g���N�X�l��o�^����N���X
     * @param value ���g���N�X�l
     * @throws MetricAlreadyRegisteredException ���ɂ��̃v���O�C�����炱�̃N���X�Ɋւ��郁�g���N�X�l�̕񍐂�����Ă���ꍇ.
     */
    protected final void registMetric(final TargetClassInfo classInfo, final Number value)
            throws MetricAlreadyRegisteredException {

        if ((null == classInfo) || (null == value)) {
            throw new NullPointerException();
        }

        if (null == this.classMetricsRegister) {
            synchronized (this) {
                if (null == this.classMetricsRegister) {
                    this.classMetricsRegister = new DefaultClassMetricsRegister(this);
                }
            }
        }
        this.classMetricsRegister.registMetric(classInfo, value);
    }

    /**
     * ���\�b�h�P�ʂ̃��g���N�X�l��o�^���郁�\�b�h.
     * 
     * @param methodInfo ���g���N�X�l��o�^���郁�\�b�h
     * @param value ���g���N�X�l
     * @throws MetricAlreadyRegisteredException ���ɂ��̃v���O�C�����炱�̃��\�b�h�Ɋւ��郁�g���N�X�l�̕񍐂�����Ă���ꍇ.
     */
    protected final void registMetric(final TargetMethodInfo methodInfo, final Number value)
            throws MetricAlreadyRegisteredException {

        if ((null == methodInfo) || (null == value)) {
            throw new NullPointerException();
        }

        if (null == this.methodMetricsRegister) {
            synchronized (this) {
                if (null == this.methodMetricsRegister) {
                    this.methodMetricsRegister = new DefaultMethodMetricsRegister(this);
                }
            }
        }
        this.methodMetricsRegister.registMetric(methodInfo, value);
    }

    /**
     * �t�B�[���h�P�ʂ̃��g���N�X�l��o�^���郁�\�b�h.
     * 
     * @param fieldInfo ���g���N�X�l��o�^���郁�\�b�h
     * @param value ���g���N�X�l
     * @throws MetricAlreadyRegisteredException ���ɂ��̃v���O�C�����炱�̃��\�b�h�Ɋւ��郁�g���N�X�l�̕񍐂�����Ă���ꍇ.
     */
    protected final void registMetric(final TargetFieldInfo fieldInfo, final Number value)
            throws MetricAlreadyRegisteredException {

        if ((null == fieldInfo) || (null == value)) {
            throw new NullPointerException();
        }

        if (null == this.fieldMetricsRegister) {
            synchronized (this) {
                if (null == this.fieldMetricsRegister) {
                    this.fieldMetricsRegister = new DefaultFieldMetricsRegister(this);
                }
            }
        }
        this.fieldMetricsRegister.registMetric(fieldInfo, value);
    }

    /**
     * ���̃v���O�C������̐i�����𑗂郁�\�b�h
     * 
     * @param percentage �i�����l
     */
    protected final void reportProgress(final int percentage) {
        if (this.reporter != null) {
            this.reporter.reportProgress(percentage);
        }
    }

    /**
     * ���̃v���O�C�����N���X�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D �f�t�H���g�����ł�false��Ԃ��D
     * �N���X�Ɋւ�����𗘗p����v���O�C���͂��̃��\�b�h���I�[�o�[���[�h����true��Ԃ��Ȃ���ΐ���Ȃ��D
     * 
     * @return �N���X�Ɋւ�����𗘗p����ꍇ��true�D
     */
    protected boolean useClassInfo() {
        return false;
    }

    /**
     * ���̃v���O�C�����t�B�[���h�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D �f�t�H���g�����ł�false��Ԃ��D
     * �t�B�[���h�Ɋւ�����𗘗p����v���O�C���͂��̃��\�b�h���I�[�o�[���[�h����true��Ԃ��Ȃ���ΐ���Ȃ��D
     * 
     * @return �t�B�[���h�Ɋւ�����𗘗p����ꍇ��true�D
     */
    protected boolean useFieldInfo() {
        return false;
    }

    /**
     * ���̃v���O�C�����t�@�C���Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D �f�t�H���g�����ł�false��Ԃ��D
     * �t�@�C���Ɋւ�����𗘗p����v���O�C���͂��̃��\�b�h���I�[�o�[���[�h����true��Ԃ��Ȃ���ΐ���Ȃ��D
     * 
     * @return �t�@�C���Ɋւ�����𗘗p����ꍇ��true�D
     */
    protected boolean useFileInfo() {
        return false;
    }

    /**
     * ���̃v���O�C�������\�b�h�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D �f�t�H���g�����ł�false��Ԃ��D
     * ���\�b�h�Ɋւ�����𗘗p����v���O�C���͂��̃��\�b�h���I�[�o�[���[�h����true��Ԃ��Ȃ���ΐ���Ȃ��D
     * 
     * @return ���\�b�h�Ɋւ�����𗘗p����ꍇ��true�D
     */
    protected boolean useMethodInfo() {
        return false;
    }

    /**
     * ���̃v���O�C�������\�b�h�����Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D �f�t�H���g�����ł�false��Ԃ��D
     * ���\�b�h�����Ɋւ�����𗘗p����v���O�C���͂��̃��\�b�h���I�[�o�[���[�h����true��Ԃ��Ȃ���ΐ���Ȃ��D
     * 
     * @return ���\�b�h�����Ɋւ�����𗘗p����ꍇ��true�D
     */
    protected boolean useMethodLocalInfo() {
        return false;
    }

    /**
     * ���s�O��̋��ʏ��������Ă���C {@link #execute()}���Ăяo��.
     */
    final synchronized void executionWrapper() {
        assert (null == this.reporter) : "Illegal state : previous reporter was not removed.";
        try {
            this.reporter = new DefaultProgressReporter(this);
        } catch (final AlreadyConnectedException e1) {
            assert (null == this.reporter) : "Illegal state : previous reporter was still connected.";
        }

        // ���̃X���b�h�Ƀp�[�~�b�V������������悤�ɗv��
        MetricsToolSecurityManager.getInstance().requestPluginPermission(this);

        try {
            this.execute();
        } catch (final Throwable e) {
            this.err.println(e);
        }

        if (null != this.reporter) {
            // �i���񍐂̏I���C�x���g�𑗂�
            // �v���O�C�����Ŋ��ɑ����Ă����牽�������ɕԂ��Ă���
            this.reporter.reportProgressEnd();
            this.reporter = null;
        }

        // ���̃X���b�h����p�[�~�b�V��������������悤�ɗv��
        MetricsToolSecurityManager.getInstance().removePluginPermission(this);
    }

    /**
     * ���b�Z�[�W�o�͗p�̃v�����^�[
     */
    protected final MessagePrinter out = new DefaultMessagePrinter(this, MESSAGE_TYPE.OUT);

    /**
     * �G���[���b�Z�[�W�o�͗p�̃v�����^�[
     */
    protected final MessagePrinter err = new DefaultMessagePrinter(this, MESSAGE_TYPE.ERROR);

    /**
     * �o�^����Ă���t�@�C�����ɃA�N�Z�X����f�t�H���g�̃A�N�Z�T.
     */
    private final FileInfoAccessor fileInfoAccessor = new DefaultFileInfoAccessor();

    /**
     * �o�^����Ă���N���X���ɃA�N�Z�X����f�t�H���g�̃A�N�Z�T.
     */
    private final ClassInfoAccessor classInfoAccessor = new DefaultClassInfoAccessor();

    /**
     * �o�^����Ă��郁�\�b�h���ɃA�N�Z�X����f�t�H���g�̃A�N�Z�T.
     */
    private final MethodInfoAccessor methodInfoAccessor = new DefaultMethodInfoAccessor();

    /**
     * �t�@�C���P�ʂ̃��g���N�X�l��o�^���郌�W�X�^.
     */
    private FileMetricsRegister fileMetricsRegister;

    /**
     * �N���X�P�ʂ̃��g���N�X�l��o�^���郌�W�X�^.
     */
    private ClassMetricsRegister classMetricsRegister;

    /**
     * ���\�b�h�P�ʂ̃��g���N�X�l��o�^���郌�W�X�^.
     */
    private MethodMetricsRegister methodMetricsRegister;
    
    /**
     * 
     */
    private FieldMetricsRegister fieldMetricsRegister; 

    /**
     * �i����񑗐M�p�̃��|�[�^�[
     */
    private ProgressReporter reporter;

    /**
     * ���̃v���O�C���̎��s���̋������p�[�~�b�V����
     */
    private final Permissions permissions = new Permissions();

    /**
     * �v���O�C���̏���ۑ�����{@link PluginInfo}�N���X�̃C���X�^���X getPluginInfo���\�b�h�̏���̌Ăяo���ɂ���č쐬����D
     * ����ȍ~�A���̃t�B�[���h�͏�ɓ����C���X�^���X���Q�Ƃ���D
     */
    private PluginInfo pluginInfo;

    /**
     * �v���O�C���̃��[�g�f�B���N�g��
     */
    private File pluginRootDir;

    /**
     * {@link MessageSource}�� {@link ProgressSource}�p�̖��O
     */
    private String sourceName = "";
}
