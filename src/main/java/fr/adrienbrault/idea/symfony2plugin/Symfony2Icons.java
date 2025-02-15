package fr.adrienbrault.idea.symfony2plugin;

import com.intellij.openapi.util.IconLoader;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Calendar;

/**
 * @author Adrien Brault <adrien.brault@gmail.com>
 * @author Daniel Espendiller <daniel@espendiller.net>
 */
public class Symfony2Icons {

    public static final Icon SYMFONY;
    public static final Icon SYMFONY_LINE_MARKER;

    static {
        if(Calendar.getInstance().get(Calendar.MONTH) == 11 && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) > 7) {
            SYMFONY = IconLoader.getIcon("/icons/symfony_dec.png", Symfony2Icons.class);
            SYMFONY_LINE_MARKER = IconLoader.getIcon("/icons/symfony_line_marker_dec.png", Symfony2Icons.class);
        } else if(Calendar.getInstance().get(Calendar.MONTH) == 3 && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 7) {
            SYMFONY = IconLoader.getIcon("/icons/symfony_birth.png", Symfony2Icons.class);
            SYMFONY_LINE_MARKER = IconLoader.getIcon("/icons/symfony_line_marker_birth.png", Symfony2Icons.class);
        } else {
            SYMFONY = IconLoader.getIcon("/icons/symfony.png", Symfony2Icons.class);
            SYMFONY_LINE_MARKER = IconLoader.getIcon("/icons/symfony_line_marker.png", Symfony2Icons.class);
        }
    }

    public static final Icon SYMFONY_TOOL_WINDOW = IconLoader.getIcon("/icons/symfony_tool_window.png", Symfony2Icons.class);
    public static final Icon DOCTRINE = IconLoader.getIcon("/icons/doctrine.png", Symfony2Icons.class);
    public static final Icon DOCTRINE_LINE_MARKER = IconLoader.getIcon("/icons/doctrine_line_marker.png", Symfony2Icons.class);
    public static final Icon DOCTRINE_WEAK = IconLoader.getIcon("/icons/doctrine_weak.png", Symfony2Icons.class);
    public static final Icon NO_SQL = IconLoader.getIcon("/icons/no_sql.png", Symfony2Icons.class);
    public static final Icon ROUTE = IconLoader.getIcon("/icons/route.png", Symfony2Icons.class);
    public static final Icon ROUTE_WEAK = IconLoader.getIcon("/icons/route_weak.png", Symfony2Icons.class);
    public static final Icon SERVICE = IconLoader.getIcon("/icons/service.png", Symfony2Icons.class);
    public static final Icon SERVICE_OPACITY = IconLoader.getIcon("/icons/service_opacity.png", Symfony2Icons.class);
    public static final Icon FORM_TYPE = IconLoader.getIcon("/icons/form_type.png", Symfony2Icons.class);
    public static final Icon FORM_TYPE_WEAK = IconLoader.getIcon("/icons/form_type_weak.png", Symfony2Icons.class);
    public static final Icon TRANSLATION = IconLoader.getIcon("/icons/translation.png", Symfony2Icons.class);
    public static final Icon TRANSLATION_WEAK = IconLoader.getIcon("/icons/translation_weak.png", Symfony2Icons.class);
    public static final Icon PARAMETER = IconLoader.getIcon("/icons/parameter.png", Symfony2Icons.class);
    public static final Icon FORM_OPTION = IconLoader.getIcon("/icons/form_option.png", Symfony2Icons.class);
    public static final Icon FORM_EXTENSION = IconLoader.getIcon("/icons/form_extension.png", Symfony2Icons.class);
    public static final Icon FORM_EXTENSION_WEAK = IconLoader.getIcon("/icons/form_extension_weak.png", Symfony2Icons.class);
    public static final Icon SERVICE_TAG = IconLoader.getIcon("/icons/service_tag.png", Symfony2Icons.class);
    public static final Icon SERVICE_TAG_WEAK = IconLoader.getIcon("/icons/service_tag_weak.png", Symfony2Icons.class);
    public static final Icon EVENT = IconLoader.getIcon("/icons/event.png", Symfony2Icons.class);
    public static final Icon BUNDLE = IconLoader.getIcon("/icons/bundle.png", Symfony2Icons.class);

    public static final Icon SERVICE_PRIVATE_OPACITY = IconLoader.getIcon("/icons/service_private_opacity.png", Symfony2Icons.class);

    public static final Icon SERVICE_LINE_MARKER = IconLoader.getIcon("/icons/service_line_marker.png", Symfony2Icons.class);
    public static final Icon FORM_TYPE_LINE_MARKER = IconLoader.getIcon("/icons/form_type_line_marker.png", Symfony2Icons.class);
    public static final Icon ROUTE_LINE_MARKER = IconLoader.getIcon("/icons/route_line_marker.png", Symfony2Icons.class);
    public static final Icon TWIG_CONTROLLER_LINE_MARKER = IconLoader.getIcon("/icons/twig_controller_line_marker.png", Symfony2Icons.class);
    public static final Icon TWIG_LINE_MARKER = IconLoader.getIcon("/icons/twig_line_marker.png", Symfony2Icons.class);
    public static final Icon TWIG_LINE_OVERWRITE = IconLoader.getIcon("/icons/overwrite.png", Symfony2Icons.class);

    public static final Icon TWIG_CONTROLLER_FILE = IconLoader.getIcon("/icons/twig_controller_file.png", Symfony2Icons.class);
    public static final Icon TWIG_EXTENDS_FILE = IconLoader.getIcon("/icons/twig_extends_file.png", Symfony2Icons.class);
    public static final Icon TWIG_IMPLEMENTS_FILE = IconLoader.getIcon("/icons/twig_implements_file.png", Symfony2Icons.class);

    public static final Icon PARAMETER_OPACITY = IconLoader.getIcon("/icons/parameter_opacity.png", Symfony2Icons.class);

    public static final Icon MAIL = IconLoader.getIcon("/icons/mail.png", Symfony2Icons.class);
    public static final Icon PROFILER_LINK = IconLoader.getIcon("/icons/profiler_link.png", Symfony2Icons.class);

    public static final Icon CONFIG_PROTOTYPE = IconLoader.getIcon("/icons/config_prototype.png", Symfony2Icons.class);
    public static final Icon CONFIG_VALUE = IconLoader.getIcon("/icons/config_value.png", Symfony2Icons.class);
    public static final Icon CONFIG_VALUE_SHORTCUT = IconLoader.getIcon("/icons/config_value_shortcut.png", Symfony2Icons.class);

    public static final Icon TWIG_BLOCK_OVERWRITE = IconLoader.getIcon("/icons/twig_block_overwrite.png", Symfony2Icons.class);

    public static Image getImage(Icon icon) {

        if (icon instanceof ImageIcon) {
            return ((ImageIcon)icon).getImage();
        }

        int width = icon.getIconWidth();
        int height = icon.getIconHeight();
        BufferedImage image = UIUtil.createImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        icon.paintIcon(null, g2, 0, 0);
        return image;
    }
}

