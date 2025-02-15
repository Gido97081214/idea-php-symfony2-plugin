package fr.adrienbrault.idea.symfony2plugin.config;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.psi.elements.*;
import fr.adrienbrault.idea.symfony2plugin.Symfony2ProjectComponent;
import fr.adrienbrault.idea.symfony2plugin.dic.ConstraintPropertyReference;
import fr.adrienbrault.idea.symfony2plugin.dic.ServiceReference;
import fr.adrienbrault.idea.symfony2plugin.doctrine.EntityHelper;
import fr.adrienbrault.idea.symfony2plugin.doctrine.EntityReference;
import fr.adrienbrault.idea.symfony2plugin.doctrine.dict.DoctrineTypes;
import fr.adrienbrault.idea.symfony2plugin.templating.TemplateReference;
import fr.adrienbrault.idea.symfony2plugin.util.MethodMatcher;
import fr.adrienbrault.idea.symfony2plugin.util.ParameterBag;
import fr.adrienbrault.idea.symfony2plugin.util.PhpElementsUtil;
import fr.adrienbrault.idea.symfony2plugin.util.PsiElementUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 */
public class SymfonyPhpReferenceContributor extends PsiReferenceContributor {

    private static MethodMatcher.CallToSignature[] CONTAINER_SIGNATURES = new MethodMatcher.CallToSignature[] {
        new MethodMatcher.CallToSignature("\\Symfony\\Bundle\\FrameworkBundle\\Controller\\Controller", "get"),
        new MethodMatcher.CallToSignature("\\Symfony\\Bundle\\FrameworkBundle\\Controller\\Controller", "has"),

        // Symfony 3.3 / 3.4
        new MethodMatcher.CallToSignature("\\Symfony\\Bundle\\FrameworkBundle\\Controller\\ControllerTrait", "get"),
        new MethodMatcher.CallToSignature("\\Symfony\\Bundle\\FrameworkBundle\\Controller\\ControllerTrait", "has"),

        // Symfony 4
        new MethodMatcher.CallToSignature("\\Symfony\\Bundle\\FrameworkBundle\\Controller\\AbstractController", "get"),
        new MethodMatcher.CallToSignature("\\Symfony\\Bundle\\FrameworkBundle\\Controller\\AbstractController", "has"),

        new MethodMatcher.CallToSignature("\\Symfony\\Component\\DependencyInjection\\ContainerInterface", "get"),
        new MethodMatcher.CallToSignature("\\Symfony\\Component\\DependencyInjection\\ContainerInterface", "has"),
        new MethodMatcher.CallToSignature("\\Psr\\Container\\ContainerInterface", "get"),
        new MethodMatcher.CallToSignature("\\Psr\\Container\\ContainerInterface", "has"),

        new MethodMatcher.CallToSignature("Symfony\\Component\\DependencyInjection\\ParameterBag\\ContainerBagInterface", "get"),
        new MethodMatcher.CallToSignature("Symfony\\Component\\DependencyInjection\\ParameterBag\\ContainerBagInterface", "has"),
    };

    public static MethodMatcher.CallToSignature[] REPOSITORY_SIGNATURES = new MethodMatcher.CallToSignature[] {
        new MethodMatcher.CallToSignature("\\Doctrine\\Common\\Persistence\\ManagerRegistry", "getRepository"),
        new MethodMatcher.CallToSignature("\\Doctrine\\Common\\Persistence\\ObjectManager", "getRepository"),
        new MethodMatcher.CallToSignature("\\Doctrine\\Persistence\\ManagerRegistry", "getRepository"),
        new MethodMatcher.CallToSignature("\\Doctrine\\Persistence\\ObjectManager", "getRepository"),
        new MethodMatcher.CallToSignature("\\Doctrine\\ORM\\EntityManager", "getReference"),
        new MethodMatcher.CallToSignature("\\Doctrine\\Common\\Persistence\\ManagerRegistry", "getManagerForClass"),
        new MethodMatcher.CallToSignature("\\Doctrine\\Persistence\\ManagerRegistry", "getManagerForClass"),
        new MethodMatcher.CallToSignature("\\Doctrine\\ORM\\QueryBuilder", "update"),
        new MethodMatcher.CallToSignature("\\Doctrine\\ORM\\QueryBuilder", "delete"),
        new MethodMatcher.CallToSignature("\\Doctrine\\ORM\\QueryBuilder", "from"),

        // doctrine 2.5 methods
        new MethodMatcher.CallToSignature("\\Doctrine\\ORM\\Cache", "getEntityCacheRegion"),
        new MethodMatcher.CallToSignature("\\Doctrine\\ORM\\Cache", "containsEntity"),
        new MethodMatcher.CallToSignature("\\Doctrine\\ORM\\Cache", "evictEntity"),
        new MethodMatcher.CallToSignature("\\Doctrine\\ORM\\Cache", "evictEntityRegion"),
        new MethodMatcher.CallToSignature("\\Doctrine\\ORM\\Cache", "containsCollection"),
        new MethodMatcher.CallToSignature("\\Doctrine\\ORM\\Cache", "evictCollection"),
        new MethodMatcher.CallToSignature("\\Doctrine\\ORM\\Cache", "evictCollectionRegion"),
    };

    public static MethodMatcher.CallToSignature[] TEMPLATE_SIGNATURES = new MethodMatcher.CallToSignature[] {
        new MethodMatcher.CallToSignature("\\Symfony\\Component\\Templating\\EngineInterface", "render"),
        new MethodMatcher.CallToSignature("\\Symfony\\Component\\Templating\\StreamingEngineInterface", "stream"),
        new MethodMatcher.CallToSignature("\\Symfony\\Bundle\\FrameworkBundle\\Templating\\EngineInterface", "renderResponse"),
        new MethodMatcher.CallToSignature("\\Symfony\\Bundle\\FrameworkBundle\\Controller\\Controller", "render"),
        new MethodMatcher.CallToSignature("\\Symfony\\Bundle\\FrameworkBundle\\Controller\\Controller", "renderView"),
        new MethodMatcher.CallToSignature("\\Symfony\\Bundle\\FrameworkBundle\\Controller\\Controller", "stream"),

        // Symfony 3.3 / 3.4
        new MethodMatcher.CallToSignature("\\Symfony\\Bundle\\FrameworkBundle\\Controller\\ControllerTrait", "render"),
        new MethodMatcher.CallToSignature("\\Symfony\\Bundle\\FrameworkBundle\\Controller\\ControllerTrait", "renderView"),
        new MethodMatcher.CallToSignature("\\Symfony\\Bundle\\FrameworkBundle\\Controller\\ControllerTrait", "stream"),

        // Symfony 4
        new MethodMatcher.CallToSignature("\\Symfony\\Bundle\\FrameworkBundle\\Controller\\AbstractController", "render"),
        new MethodMatcher.CallToSignature("\\Symfony\\Bundle\\FrameworkBundle\\Controller\\AbstractController", "stream"),
        new MethodMatcher.CallToSignature("\\Symfony\\Bundle\\FrameworkBundle\\Controller\\AbstractController", "renderView"),

        new MethodMatcher.CallToSignature("\\Twig_Environment", "render"),
        new MethodMatcher.CallToSignature("\\Twig_Environment", "loadTemplate"),
        new MethodMatcher.CallToSignature("\\Twig_Environment", "getTemplateClass"),
        new MethodMatcher.CallToSignature("\\Twig_Environment", "display"),
        new MethodMatcher.CallToSignature("\\Twig_Environment", "isTemplateFresh"),
        new MethodMatcher.CallToSignature("\\Twig_Environment", "resolveTemplate"), // @TODO: also "is_array($names)"

        new MethodMatcher.CallToSignature("\\Twig\\Environment", "render"),
        new MethodMatcher.CallToSignature("\\Twig\\Environment", "load"),
        new MethodMatcher.CallToSignature("\\Twig\\Environment", "loadTemplate"),
        new MethodMatcher.CallToSignature("\\Twig\\Environment", "getTemplateClass"),
        new MethodMatcher.CallToSignature("\\Twig\\Environment", "display"),
        new MethodMatcher.CallToSignature("\\Twig\\Environment", "isTemplateFresh"),
        new MethodMatcher.CallToSignature("\\Twig\\Environment", "resolveTemplate"), // @TODO: also "is_array($names)"
    };

    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar psiReferenceRegistrar) {

        psiReferenceRegistrar.registerReferenceProvider(
            PhpElementsUtil.getMethodWithFirstStringOrNamedArgumentPattern(),
            new PsiReferenceProvider() {
                @NotNull
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {

                    if (MethodMatcher.getMatchedSignatureWithDepth(psiElement, CONTAINER_SIGNATURES) == null) {
                        return new PsiReference[0];
                    }

                    return new PsiReference[]{ new ServiceReference((StringLiteralExpression) psiElement, false) };
                }

                @Override
                public boolean acceptsTarget(@NotNull PsiElement target) {
                    return Symfony2ProjectComponent.isEnabled(target);
                }
            }
        );
        psiReferenceRegistrar.registerReferenceProvider(
            PhpElementsUtil.getMethodWithFirstStringOrNamedArgumentPattern(),
            new PsiReferenceProvider() {
                @NotNull
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {

                    if (MethodMatcher.getMatchedSignatureWithDepth(psiElement, TEMPLATE_SIGNATURES) == null) {
                        return new PsiReference[0];
                    }

                    return new PsiReference[]{ new TemplateReference((StringLiteralExpression) psiElement) };
                }

                @Override
                public boolean acceptsTarget(@NotNull PsiElement target) {
                    return Symfony2ProjectComponent.isEnabled(target);
                }
            }
        );

        psiReferenceRegistrar.registerReferenceProvider(
                PhpElementsUtil.getMethodWithFirstStringOrNamedArgumentPattern(),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {

                        MethodMatcher.MethodMatchParameter methodMatchParameter = MethodMatcher.getMatchedSignatureWithDepth(psiElement, REPOSITORY_SIGNATURES);
                        if (methodMatchParameter == null) {
                            return new PsiReference[0];
                        }

                        DoctrineTypes.Manager manager = EntityHelper.getManager(methodMatchParameter.getMethodReference());
                        if(manager != null) {
                            return new PsiReference[]{ new EntityReference((StringLiteralExpression) psiElement, manager) };
                        }

                        return new PsiReference[]{ new EntityReference((StringLiteralExpression) psiElement) };
                    }

                    @Override
                    public boolean acceptsTarget(@NotNull PsiElement target) {
                        return Symfony2ProjectComponent.isEnabled(target);
                    }
                }
        );

        psiReferenceRegistrar.registerReferenceProvider(
            PhpElementsUtil.getMethodWithFirstStringOrNamedArgumentPattern(),
            new PsiReferenceProvider() {
                @NotNull
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {

                    MethodMatcher.MethodMatchParameter methodMatchParameter = new MethodMatcher.StringParameterMatcher(psiElement, 0)
                        .withSignature("\\Doctrine\\Common\\Persistence\\ObjectManager", "find")
                        .withSignature("\\Doctrine\\Persistence\\ObjectManager", "find")
                        .match();

                    if(methodMatchParameter == null) {
                        return new PsiReference[0];
                    }

                    DoctrineTypes.Manager manager = EntityHelper.getManager(methodMatchParameter.getMethodReference());
                    if(manager != null) {
                        return new PsiReference[]{ new EntityReference((StringLiteralExpression) psiElement, manager) };
                    }

                    return new PsiReference[]{ new EntityReference((StringLiteralExpression) psiElement) };
                }

                @Override
                public boolean acceptsTarget(@NotNull PsiElement target) {
                    return Symfony2ProjectComponent.isEnabled(target);
                }
            }
        );

        psiReferenceRegistrar.registerReferenceProvider(
            PlatformPatterns.psiElement(StringLiteralExpression.class),
            new PsiReferenceProvider() {
                @NotNull
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {

                    if (!Symfony2ProjectComponent.isEnabled(psiElement)) {
                        return new PsiReference[0];
                    }

                    PhpClass phpClass = getClassArrayCreationParameter(psiElement);
                    if(phpClass != null) {
                        return new PsiReference[] { new ConstraintPropertyReference((StringLiteralExpression) psiElement, phpClass)};
                    }

                    return new PsiReference[0];
                }

                private PhpClass getClassArrayCreationParameter(PsiElement psiElement) {
                    ArrayCreationExpression arrayCreationExpression = PhpElementsUtil.getCompletableArrayCreationElement(psiElement);
                    if (arrayCreationExpression != null) {

                        PsiElement parameterList = arrayCreationExpression.getContext();
                        if (parameterList instanceof ParameterList) {
                            PsiElement[] methodParameters = ((ParameterList) parameterList).getParameters();
                            if (methodParameters.length > 0) {
                                PsiElement newExpression = parameterList.getContext();
                                if (newExpression instanceof NewExpression) {
                                    ParameterBag currentIndex = PsiElementUtils.getCurrentParameterIndex(arrayCreationExpression);
                                    if (currentIndex != null && currentIndex.getIndex() == 0) {
                                        return PhpElementsUtil.getNewExpressionPhpClassWithInstance((NewExpression) newExpression, "\\Symfony\\Component\\Validator\\Constraint");
                                    }
                                }
                            }
                        }
                    }

                    return null;
                }

                @Override
                public boolean acceptsTarget(@NotNull PsiElement target) {
                    return Symfony2ProjectComponent.isEnabled(target);
                }
            }
        );
    }
}
