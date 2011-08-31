package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.security.AccessControlException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.ConcurrentHashSet;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * �v���O�C���C���X�^���X���Ǘ�����N���X�D
 * �V���O���g���p�^�[���Ŏ�������Ă���D
 * 
 * @author kou-tngt
 */
public class PluginManager {

    /**
     * �v���O�C����o�^����
     * ���̃��\�b�h���Ăяo���ɂ͓��ʌ������K�v�ł���
     * @param plugin �o�^����v���O�C��
     * @throws AccessControlException ���ʌ����X���b�h�łȂ��ꍇ
     * @throws NullPointerException plugin��null�̏ꍇ
     */
    public void addPlugin(final AbstractPlugin plugin) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == plugin) {
            throw new NullPointerException("plugin is null.");
        }

        this.plugins.add(plugin);
        final PluginInfo info = plugin.getPluginInfo();
        this.pluginInfos.add(info);
        this.info2pluginMap.put(info, plugin);

        METRIC_TYPE type = plugin.getMetricType();
        switch (type) {
        case FILE_METRIC:
            this.filePlugins.add(plugin);
            break;
        case CLASS_METRIC:
            this.classPlugins.add(plugin);
            break;
        case METHOD_METRIC:
            this.methodPlugins.add(plugin);
            break;
        case FIELD_METRIC:
            this.fieldPlugins.add(plugin);
            break;
        }
    }

    /**
     * �v���O�C����o�^����
     * ���̃��\�b�h���Ăяo���ɂ͓��ʌ������K�v�ł���
     * @param collection �o�^����v���O�C���Q�����R���N�V����
     * @throws AccessControlException ���ʌ����X���b�h�łȂ��ꍇ
     * @throws NullPointerException collection��null�̏ꍇ
     */
    public void addPlugins(final Collection<AbstractPlugin> collection) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == collection) {
            throw new NullPointerException("collection is null.");
        }

        for (final AbstractPlugin plugin : collection) {
            this.addPlugin(plugin);
        }
    }

    /**
     * �v���O�C�������L�[�ɂ��āC�Ή�����v���O�C���C���X�^���X��Ԃ�.
     * ���ʌ��������X���b�h�ȊO����͌Ăяo���Ȃ�
     * @param info �L�[�ƂȂ�v���O�C�����
     * @return �Ή�����v���O�C��
     * @throws AccessControlException ���ʌ����������Ă��Ȃ��X���b�h����̌Ăяo���̏ꍇ
     */
    public AbstractPlugin getPlugin(final PluginInfo info) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        return this.info2pluginMap.get(info);
    }

    /**
     * �o�^����Ă���v���O�C���̐���Ԃ�.
     * @return �o�^����Ă���v���O�C���̐�.
     */
    public int getPluginCount() {
        return this.plugins.size();
    }

    /**
     * �S�Ẵv���O�C�����܂񂾕ҏW�s��Set��Ԃ�
     * ���ʌ��������X���b�h�ȊO����͌Ăяo���Ȃ�
     * @return �v���O�C����Set
     * @throws AccessControlException ���ʌ����������Ă��Ȃ��X���b�h����̌Ăяo���̏ꍇ
     */
    public Set<AbstractPlugin> getPlugins() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        return Collections.unmodifiableSet(this.plugins);
    }

    /**
     * �t�@�C���P�ʂ̃��g���N�X���v������v���O�C���̕ҏW�s��Set��Ԃ�
     * ���ʌ��������X���b�h�ȊO����͌Ăяo���Ȃ�
     * @return �t�@�C���P�ʂ̃��g���N�X���v������v���O�C����Set
     * @throws AccessControlException ���ʌ����������Ă��Ȃ��X���b�h����̌Ăяo���̏ꍇ
     */
    public Set<AbstractPlugin> getFileMetricPlugins() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        return Collections.unmodifiableSet(this.filePlugins);
    }

    /**
     * �N���X�P�ʂ̃��g���N�X���v������v���O�C���̕ҏW�s��Set��Ԃ�
     * ���ʌ��������X���b�h�ȊO����͌Ăяo���Ȃ�
     * @return �N���X�P�ʂ̃��g���N�X���v������v���O�C����Set
     * @throws AccessControlException ���ʌ����������Ă��Ȃ��X���b�h����̌Ăяo���̏ꍇ
     */
    public Set<AbstractPlugin> getClassMetricPlugins() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        return Collections.unmodifiableSet(this.classPlugins);
    }

    /**
     * ���\�b�h�P�ʂ̃��g���N�X���v������v���O�C���̕ҏW�s��Set��Ԃ�
     * ���ʌ��������X���b�h�ȊO����͌Ăяo���Ȃ�
     * @return ���\�b�h�P�ʂ̃��g���N�X���v������v���O�C����Set
     * @throws AccessControlException ���ʌ����������Ă��Ȃ��X���b�h����̌Ăяo���̏ꍇ
     */
    public Set<AbstractPlugin> getMethodMetricPlugins() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        return Collections.unmodifiableSet(this.methodPlugins);
    }

    /**
     * �t�B�[���h�P�ʂ̃��g���N�X���v������v���O�C���̕ҏW�s��Set��Ԃ�
     * ���ʌ��������X���b�h�ȊO����͌Ăяo���Ȃ�
     * @return �t�B�[���h�P�ʂ̃��g���N�X���v������v���O�C����Set
     * @throws AccessControlException ���ʌ����������Ă��Ȃ��X���b�h����̌Ăяo���̏ꍇ
     */
    public Set<AbstractPlugin> getFieldMetricPlugins() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        return Collections.unmodifiableSet(this.fieldPlugins);
    }

    /**
     * �v���O�C�����̕ҏW�s��Set��Ԃ�
     * @return �v���O�C������Set
     */
    public Set<PluginInfo> getPluginInfos() {
        return Collections.unmodifiableSet(this.pluginInfos);
    }

    /**
     * �v���O�C�����폜����
     * ���ʌ����X���b�h�݂̂���Ăяo����.
     * @param plugin �폜����v���O�C��
     * @throws AccessControlException ���ʌ����������Ă��Ȃ��ꍇ
     */
    public void removePlugin(final AbstractPlugin plugin) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null != plugin) {
            this.plugins.remove(plugin);
            final PluginInfo info = plugin.getPluginInfo();
            this.pluginInfos.remove(info);
            this.info2pluginMap.remove(info);

            switch (plugin.getMetricType()) {
            case FILE_METRIC:
                this.filePlugins.remove(plugin);
                break;
            case CLASS_METRIC:
                this.classPlugins.remove(plugin);
                break;
            case METHOD_METRIC:
                this.methodPlugins.remove(plugin);
                break;
            case FIELD_METRIC:
                this.fieldPlugins.remove(plugin);
            }
        }
    }

    /**
     * �v���O�C�����폜����
     * ���ʌ����X���b�h�݂̂���Ăяo����.
     * @param plugins �폜����v���O�C����Collection
     * @throws AccessControlException ���ʌ����������Ă��Ȃ��ꍇ
     */
    public void removePlugins(final Collection<AbstractPlugin> plugins) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (plugins != null) {
            for (final AbstractPlugin plugin : plugins) {
                this.removePlugin(plugin);
            }
        }
    }

    /**
     * �V���O���g���p�C���private�R���X�g���N�^
     */
    public PluginManager() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.plugins = new ConcurrentHashSet<AbstractPlugin>();
        this.filePlugins = new ConcurrentHashSet<AbstractPlugin>();
        this.classPlugins = new ConcurrentHashSet<AbstractPlugin>();
        this.methodPlugins = new ConcurrentHashSet<AbstractPlugin>();
        this.fieldPlugins = new ConcurrentHashSet<AbstractPlugin>();
        this.pluginInfos = new ConcurrentHashSet<PluginInfo>();
        this.info2pluginMap = new ConcurrentHashMap<PluginInfo, AbstractPlugin>();
    };

    /**
     * �S�Ẵv���O�C����Set
     */
    private final Set<AbstractPlugin> plugins;

    /**
     * �t�@�C���P�ʂ̃��g���N�X���v������v���O�C���̃Z�b�g
     */
    private final Set<AbstractPlugin> filePlugins;

    /**
     * �N���X�P�ʂ̃��g���N�X���v������v���O�C���̃Z�b�g
     */
    private final Set<AbstractPlugin> classPlugins;

    /**
     * ���\�b�h�P�ʂ̃��g���N�X���v������v���O�C���̃Z�b�g
     */
    private final Set<AbstractPlugin> methodPlugins;

    /**
     * �t�B�[���h�P�ʂ̃��g���N�X���v������v���O�C���̃Z�b�g
     */
    private final Set<AbstractPlugin> fieldPlugins;

    /**
     * �S�Ẵv���O�C������Set
     */
    private final Set<PluginInfo> pluginInfos;

    /**
     * �v���O�C����񂩂�v���O�C���C���X�^���X�ւ̃}�b�s���O
     */
    private final Map<PluginInfo, AbstractPlugin> info2pluginMap;
}
