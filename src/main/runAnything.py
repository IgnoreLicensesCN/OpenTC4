from copyreg import constructor

b = """    private MultiBlockWithWandClickRecipe wandClickRecipe = null;
    private CrucibleRecipe crucibleRecipe = null;
    private ShapedArcaneRecipe shapedArcaneRecipe = null;
    private ShapelessArcaneRecipe shapelessArcaneRecipe = null;
    private InfusionEnchantmentRecipe infusionEnchantmentRecipe = null;
    private InfusionRunicAugmentRecipe infusionRunicAugmentRecipe = null;
    private InfusionRecipe infusionRecipe = null;
    private ArcaneSceptreRecipe arcaneSceptreRecipe = null;
    private ArcaneWandRecipe arcaneWandRecipe = null;
    private IArcaneRecipe arcaneRecipe = null;
    private IArcaneRecipe[] arcaneRecipeArray = null;
    private IRecipe vanillaLikeRecipe = null;
    private IRecipe[] vanillaLikeRecipeArray = null;"""
a:list[str] = b.split('\n')

method_validRecipe_body:list[str] = ['public Object getValidRecipe(){']
constructors:list[str] = []
constructorBodyForAnyObj:list[str] = ['''    public RecipeWrapper(Object unknownObject){''']
for i in a:
    i = i.split(' ')
    while '' in i:
        i.remove('')
    constructors.append('''    public RecipeWrapper(%s %s){
        this.%s = %s;
    }'''% (i[1], i[2], i[2], i[2]))
    constructorBodyForAnyObj.append('        if (unknownObject instanceof %s){%s=(%s)unknownObject;return;}' % (i[1], i[2], i[1]))
    method_validRecipe_body.append('''        if (%s != null){
            return %s;
        }'''%(i[2], i[2]))

constructorBodyForAnyObj.append('''        this.unknownObject = unknownObject;
    }''')
method_validRecipe_body.append('''
        return unknownObject;
    }''')
print('public class RecipeWrapper {')
b += '''
    private Object unknownObject = null;'''
print(b)
for s in constructors:
    print(s)
for s in constructorBodyForAnyObj:
    print(s)
for s in method_validRecipe_body:
    print(s)
print('}')
