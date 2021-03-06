export interface MappingValues {
    OriginTissue: string;
    TumorType: string;
    SampleDiagnosis: string;
    DataSource: string;
}

export interface Mapping {
    entityId: number;
    entityType: string;
    mappingLabels: string[];
    mappingValues: MappingValues;
    mappedTerm?: any;
    mapType?: any;
    justification?: any;
    status: string;
    suggestedMappings: any[];
    dateCreated?: any;
    dateUpdated?: any;
}

export interface MappingInterface {
    mappings: Mapping[];
}


