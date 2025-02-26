
export type Resource = {
    name: string;
    amount: number;
    cap: number;
}


export type Occupation = {
    name: string;
    count: number;
    cap: number;
}

export type Population = {
    name: string;
    count: number;
    cap: number;
    occupations: Occupation[];
    freePopulation: number;
};


export type GameDescription = {
    populations: Population[];
    resources: Resource[];
}
