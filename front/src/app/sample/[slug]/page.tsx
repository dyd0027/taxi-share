
type Props = {
    params: {
        slug: string;
    }
}
export default function slugPage(Props: Props) {
    return <h1>This is the {Props.params.slug} page</h1>;
}

// next.js에서 제공되는 기본 함수로 이렇게 하면 /aa, /bb경로는 동적으로 미리 만들어 줘서 성능이 좋아짐
export function generateStaticParam() {
    const products = ['aa', 'bb'];
    return products.map((item) => ({
        slug: item,
    }));
}